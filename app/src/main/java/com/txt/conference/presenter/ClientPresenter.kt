package com.txt.conference.presenter
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.common.utlis.ULog
import com.intel.webrtc.base.*
import com.intel.webrtc.conference.*
import com.intel.webrtc.conference.PublishOptions
import com.txt.conference.model.ClientModel
import com.txt.conference.model.IClientModel
import com.txt.conference.view.IClientView
import com.txt.conference_common.WoogeenSurfaceRenderer
import org.webrtc.EglBase
import org.webrtc.PeerConnection.IceServer
import org.webrtc.RendererCommon
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Created by pc on 2017/10/15.
 */

class ClientPresenter : ConferenceClient.ConferenceClientObserver,
        View.OnClickListener, RemoteMixedStream.RemoteMixedStreamObserver {
    private var mContext: Activity?
    private var clientView: IClientView?
    private var clientModel: IClientModel? = null

    private var remoteStreamRenderer: WoogeenSurfaceRenderer? = null
    private var localStreamRenderer: WoogeenSurfaceRenderer? = null
    private var localStream: LocalCameraStream? = null
    private var screenStream: LocalScreenStream? = null

    private var currentRemoteStream: RemoteStream? = null
    private val subscribedStreams = ArrayList<RemoteStream>()


    private var sslContext: SSLContext? = null
    private var hostnameVerifier: HostnameVerifier? = null
    private var mRoom: ConferenceClient? = null
    private var rootEglBase: EglBase? = null

    private var roomThread: HandlerThread? = null
    private var roomHandler: RoomHandler? = null

    private var originAudioMode: Int = 0
    private var statsTimer: Timer? = null
    private var cameraID = 1

    constructor(context: Activity, view: IClientView) {
        mContext = context
        clientView = view
        clientModel = ClientModel()
    }

    fun init() {
        var audioManager = mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.isSpeakerphoneOn = true
        originAudioMode = audioManager.mode
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(
                        AudioManager.STREAM_VOICE_CALL) / 4,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        mContext?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setUpINSECURESSLContext()
        initRoom()
        initVideoStreamsViews()
    }


    private fun initRoom() {
        rootEglBase = EglBase.create()
        ClientContext.setApplicationContext(mContext)
        ClientContext.setVideoHardwareAccelerationOptions(rootEglBase!!.eglBaseContext,
                rootEglBase!!.eglBaseContext)
        val config = ConferenceClientConfiguration()
        val iceServers = ArrayList<IceServer>()
        try {
            config.iceServers = iceServers
        } catch (e1: WoogeenException) {
            e1.printStackTrace()
        }

        mRoom = ConferenceClient(config)
        mRoom?.addObserver(this)

        roomThread = HandlerThread("Room Thread")
        roomThread?.start()
        roomHandler = RoomHandler(roomThread!!.looper)
    }


    fun initVideoStreamsViews() {
//        remoteViewContainer = mView?.getRemoteViewContainer()

        localStreamRenderer = WoogeenSurfaceRenderer(mContext)
        remoteStreamRenderer = WoogeenSurfaceRenderer(mContext)
        localStreamRenderer?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);

        clientView?.addRemoteView(remoteStreamRenderer!!)

        localStreamRenderer?.init(rootEglBase?.eglBaseContext, null)
        remoteStreamRenderer?.init(rootEglBase?.eglBaseContext, null)
    }


    fun joinRoom(token: String?) {
        init()
        val connectionOptions = ConnectionOptions()
        connectionOptions.sslContext = sslContext
        connectionOptions.hostnameVerifier = hostnameVerifier
        ULog.d(TAG, "use $token to join room" )
        mRoom!!.join(token, connectionOptions, object : ActionCallback<User> {

            override fun onSuccess(p0: User?) {
                mContext?.runOnUiThread {
                    Toast.makeText(mContext, "Room Connected",
                            Toast.LENGTH_SHORT).show()
                    val msg = roomHandler?.obtainMessage()
                    msg?.what = PUBLISH_STREAM
                    roomHandler?.sendMessage(msg)
                }
                statsTimer = Timer()
                statsTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        val msg = roomHandler?.obtainMessage()
                        msg?.what = STATUS_REMOTE
                        roomHandler?.sendMessage(msg)
                    }
                }, 1000, 3000)
                var users = mRoom?.users
                ULog.d(TAG, "user size: " + users?.size)
                for (i in 0..users!!.size-1) {
                    ULog.d(TAG, "userName: " + users?.get(i).name + " role:" + users?.get(i).role)
                }
                clientView?.setAlreadyAttendees(mRoom?.users?.size.toString())
                clientView?.updateUsers(clientModel?.getUsers(mRoom?.users as List<User>)!!)
            }

            override fun onFailure(p0: WoogeenException?) {
                mContext?.runOnUiThread {
                    Toast.makeText(mContext,
                            "onFailure " + p0!!.message, Toast.LENGTH_SHORT).show()
                }
            }

        })


    }


    //******************WARNING****************
    //DO NOT IMPLEMENT THIS IN PRODUCTION CODE
    //*****************************************
    private fun setUpINSECURESSLContext() {
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })

        hostnameVerifier = HostnameVerifier { hostname, session -> true }

        try {
            sslContext = SSLContext.getInstance("TLS")
            sslContext!!.init(null, trustManagers, null)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

    }


    override fun onStreamAdded(remoteStream: RemoteStream?) {
        ULog.d(TAG, "onStreamAdded: streamId = " + remoteStream?.getId()
                + ", from " + remoteStream?.remoteUserId);
        if (localStream != null && remoteStream?.id == localStream!!.id) {
            return
        }

        if (screenStream != null && remoteStream?.id == screenStream!!.id) {
            return
        }

        //We only subscribe the "common" mix stream and screen stream by default
        if (remoteStream is RemoteMixedStream && remoteStream.viewport == "common") {
//            showResolutionSelect()
            val msg = Message()
            msg.what = MSG_SUBSCRIBE
            msg.obj = remoteStream
            roomHandler?.sendMessage(msg)
        } else if (remoteStream is RemoteScreenStream) {
            val msg = Message()
            msg.what = MSG_SUBSCRIBE
            msg.obj = remoteStream
            roomHandler!!.sendMessage(msg)
        }
    }

    override fun onUserJoined(p0: User?) {
        ULog.d(TAG, "onUserJoined")
        clientView?.setAlreadyAttendees(mRoom?.users?.size.toString())
    }

    override fun onServerDisconnected() {
        ULog.d(TAG, "onServerDisconnected")
    }

    override fun onMessageReceived(p0: String?, p1: String?, p2: Boolean) {
        ULog.d(TAG, "onMessageReceived")
    }

    override fun onStreamRemoved(remoteStream: RemoteStream?) {
        ULog.d(TAG, "onStreamRemoved: streamId = " + remoteStream?.id)
        if (currentRemoteStream != null && currentRemoteStream?.id.equals(remoteStream?.id)) {
            val msg = Message()
            msg.what = MSG_UNSUBSCRIBE
            msg.obj = remoteStream
            roomHandler?.sendMessage(msg)
            currentRemoteStream = null
            remoteStreamRenderer?.cleanFrame()
        }
        for (stream in subscribedStreams){
            if(stream?.id.equals(remoteStream?.id)){
                subscribedStreams.remove(stream)
                break
            }
        }
        if (subscribedStreams.size == 0 || currentRemoteStream != null) {
            return
        }
        // If there is another remote stream subscribed, render it.
        var streamToBeRendered=subscribedStreams[0]
        for (stream in subscribedStreams){
            if(stream is RemoteStream){
                streamToBeRendered=stream
                break
            }
        }
        try {
            currentRemoteStream = streamToBeRendered;
            mRoom?.playVideo(currentRemoteStream, null);
            currentRemoteStream?.attach(remoteStreamRenderer);
        } catch (e:WoogeenIllegalArgumentException) {
            e.printStackTrace()
        }

    }

    override fun onUserLeft(p0: User?) {
        ULog.d(TAG, "onUserLeft")
        clientView?.setAlreadyAttendees(mRoom?.users?.size.toString())
    }

    override fun onStreamError(p0: Stream?, p1: WoogeenException?) {
        ULog.d(TAG, "onStreamError")
    }

    override fun onClick(v: View?) {

    }

    override fun onVideoLayoutChanged() {
        ULog.d(TAG, "onVideoLayoutChanged")

    }


    inner class RoomHandler : Handler {

        constructor(looper: Looper) : super(looper)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                PUBLISH_STREAM -> {
                    var cameraType = LocalCameraStreamParameters.getCameraList()
                    val cameraNum = cameraType.size
                    if (cameraNum == 0) {
                        Toast.makeText(mContext, "You do not have a camera.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val cameraLists = arrayOfNulls<String>(cameraNum)
                    for (i in 0..cameraNum - 1) {
                        if (cameraType[i] == LocalCameraStreamParameters.CameraType.BACK) {
                            cameraLists[i] = "Back"
                        } else if (cameraType[i] == LocalCameraStreamParameters.CameraType.FRONT) {
                            cameraLists[i] = "Front"
                        } else if (cameraType[i] == LocalCameraStreamParameters.CameraType.UNKNOWN) {
                            cameraLists[i] = "Unknown"
                        }
                    }
                    publish()
                }

                MSG_SUBSCRIBE -> {
                    var option = SubscribeOptions()
                    option.videoCodec = MediaCodec.VideoCodec.H264
                    var remoteStream = msg.obj as RemoteStream
                    if (remoteStream is RemoteMixedStream) {
                        option.setResolution(640, 480)

                    }
                    mRoom!!.subscribe(remoteStream, option, object : ActionCallback<RemoteStream> {
                        override fun onSuccess(remoteStream: RemoteStream) {
                            ULog.d(TAG, "onStreamSubscribed")
                            try {
                                mContext?.runOnUiThread {
                                    remoteStreamRenderer!!.setScalingType(
                                            RendererCommon.ScalingType.SCALE_ASPECT_FIT)
                                    val vto = remoteStreamRenderer!!.viewTreeObserver
                                    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
                                        override fun onGlobalLayout() {
                                            remoteStreamRenderer!!.viewTreeObserver
                                                    .removeGlobalOnLayoutListener(this)
                                            remoteStreamRenderer!!.layoutParams = LinearLayout.LayoutParams(
                                                    remoteStreamRenderer!!.measuredWidth,
                                                    remoteStreamRenderer!!.measuredHeight)
                                        }
                                    }
                                    vto.addOnGlobalLayoutListener(listener)
                                    ULog.d(TAG, "Subscribed stream: " + remoteStream.id)
                                }
                                subscribedStreams.add(remoteStream)
                                if (currentRemoteStream != null) {
                                    if (currentRemoteStream is RemoteScreenStream) {
                                        mRoom!!.pauseVideo(remoteStream, null)
                                        return
                                    }
                                    mRoom!!.pauseVideo(currentRemoteStream, null)
                                    currentRemoteStream!!.detach(remoteStreamRenderer!!)
                                }
                                currentRemoteStream = remoteStream
                                currentRemoteStream!!.attach(remoteStreamRenderer!!)
                            } catch (e: WoogeenException) {
                                e.printStackTrace()
                            }

                        }

                        override fun onFailure(e: WoogeenException) {
                            e.printStackTrace()
                        }

                    })
                }
                MSG_UNSUBSCRIBE -> {
                    val stream = msg.obj as RemoteStream
                    mRoom?.unsubscribe(stream,object:ActionCallback<Void>{
                        override fun onFailure(p0: WoogeenException?) {
                            p0?.printStackTrace()
                        }
                        override fun onSuccess(p0: Void?) {
                            subscribedStreams.remove(stream)
                        }
                    })
                }

                MSG_PUBLISH -> {
                    try {
                        val msp = LocalCameraStreamParameters(true, true, true)
                        msp.setResolution(640, 480)
                        msp.setCameraId(cameraID)
                        //To set the video frame filter.
                        //WoogeenBrightenFilter is a simple filter for brightening the image.
                        //LocalCameraStream.setFilter(WoogeenBrightenFilter.create(rootEglBase
                        // .getEglBaseContext()));
                        localStream = LocalCameraStream(msp)
                        localStream?.attach(localStreamRenderer!!)
                        localStreamRenderer?.setMirror(LocalCameraStreamParameters
                                .getCameraList()[cameraID] == LocalCameraStreamParameters.CameraType.FRONT)
                        val option = PublishOptions()
                        option.maximumVideoBandwidth = Integer.MAX_VALUE
                        //Be careful when you set up the audio bandwidth, as different audio
                        // codecs require different minimum bandwidth.
                        option.maximumAudioBandwidth = Integer.MAX_VALUE
                        option.videoCodec = MediaCodec.VideoCodec.H264
                        if (localStream != null) {
                            mRoom?.publish(localStream, option, object : ActionCallback<Void> {

                                override fun onSuccess(result: Void?) {
                                    clientModel?.cameraIsOpen = true
                                    clientView?.onOffCamera(clientModel?.cameraIsOpen!!)
                                }

                                override fun onFailure(e: WoogeenException) {
                                    if (localStream != null) {
                                        localStream!!.close()
                                        localStreamRenderer!!.cleanFrame()
                                        localStream = null
                                    }
                                    e.printStackTrace()
                                }

                            })
                        }

                    } catch (e: WoogeenException) {
                        if (localStream != null) {
                            localStream!!.close()
                            localStreamRenderer!!.cleanFrame()
                            localStream = null
                        }
                        e.printStackTrace()
                    }

                }

                MSG_UNPUBLISH -> {
                    if (localStream != null) {
                        mRoom?.unpublish(localStream, object : ActionCallback<Void> {
                            override fun onSuccess(p0: Void?) {
                                localStream?.close()
                                localStream = null
                                localStreamRenderer?.cleanFrame()
                                clientModel?.cameraIsOpen = false
                                clientView?.onOffCamera(clientModel?.cameraIsOpen!!)
                            }

                            override fun onFailure(p0: WoogeenException?) {

                            }
                        })
                    }
                }

                MSG_ROOM_DISCONNECTED -> {
                    mRoom?.leave(object : ActionCallback<Void> {
                        override fun onSuccess(p0: Void?) {
                            ULog.d(TAG, "leave success")
                            localStream?.close()
                            localStream = null
                            statsTimer?.cancel()
                        }

                        override fun onFailure(p0: WoogeenException?) {
                            ULog.e(TAG, "leave failure ", p0)
                        }
                    })
                }

                MSG_SWITCHCAMERA -> {
                    if (localStream == null) {
                        return
                    }
                    localStream?.switchCamera(object : ActionCallback<Boolean> {
                        override fun onSuccess(isFrontCamera: Boolean?) {
                            ULog.d(TAG, "switchCamera onSuccess isFrontCamera $isFrontCamera")
                            clientView?.switchCamera(isFrontCamera!!)
                            localStreamRenderer?.setMirror(isFrontCamera!!)
                        }

                        override fun onFailure(p0: WoogeenException?) {
                            ULog.e(TAG, "switchCamera onFailure isFrontCamera", p0)
                        }
                    })
                }
            }
        }

    }


    fun onResume() {
        if (localStream != null) {
            localStream!!.enableVideo()
            localStream!!.enableAudio()
            try {
                localStream!!.attach(localStreamRenderer!!)
            } catch (e: WoogeenIllegalArgumentException) {
                e.printStackTrace()
            }

            Toast.makeText(mContext, "Welcome back", Toast.LENGTH_SHORT).show()
        }
        if (currentRemoteStream != null) {
            currentRemoteStream!!.enableVideo()
            currentRemoteStream!!.enableAudio()
            try {
                currentRemoteStream!!.attach(remoteStreamRenderer!!)
            } catch (e: WoogeenIllegalArgumentException) {
                e.printStackTrace()
            }

        }
        (mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager).mode = AudioManager.MODE_IN_COMMUNICATION
    }

    fun onPause() {
        if (localStream != null) {
            localStream?.disableVideo()
            localStream?.disableAudio()
            localStream?.detach()
            mContext?.runOnUiThread {
                Toast.makeText(mContext, "Woogeen is running in the background.",
                        Toast.LENGTH_SHORT).show()
            }
        }
        if (currentRemoteStream != null) {
            currentRemoteStream?.disableAudio()
            currentRemoteStream?.disableVideo()
            currentRemoteStream?.detach()
        }
        (mContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager).setMode(originAudioMode)
    }

    private fun unPublish() {
        roomHandler?.sendEmptyMessage(MSG_UNPUBLISH)
    }

    private fun publish() {
        roomHandler?.sendEmptyMessage(MSG_PUBLISH)
    }

    private fun leave() {
        roomHandler?.sendEmptyMessage(MSG_ROOM_DISCONNECTED)
    }

    fun onOffcamera() {
        if (clientModel?.cameraIsOpen!!) unPublish() else publish()
    }

    fun switchCamera() {
        roomHandler?.sendEmptyMessage(MSG_SWITCHCAMERA)
    }

    fun onStop() {

    }

    fun dettach() {
        leave()
        mContext = null
        clientView = null
    }



    companion object {
        val MSG_ROOM_DISCONNECTED = 98
        val MSG_UNPUBLISH = 103
        val MSG_SWITCHCAMERA = 108
        val STATUS_REMOTE = 114
        val PUBLISH_STREAM = 112
        val MSG_PUBLISH = 113
        val MSG_SUBSCRIBE = 116
        val MSG_UNSUBSCRIBE = 120
        val TAG: String = ClientPresenter::class.java.simpleName
    }
}