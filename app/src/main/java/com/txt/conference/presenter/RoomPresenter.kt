package com.txt.conference.presenter

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.intel.webrtc.base.ClientContext
import com.intel.webrtc.base.RemoteStream
import com.intel.webrtc.base.Stream
import com.intel.webrtc.base.WoogeenException
import com.intel.webrtc.conference.ConferenceClient
import com.intel.webrtc.conference.ConferenceClientConfiguration
import com.intel.webrtc.conference.ConnectionOptions
import com.intel.webrtc.conference.User
import com.txt.conference.view.IRoomView
import com.txt.conference_common.WoogeenSurfaceRenderer
import org.webrtc.EglBase
import org.webrtc.PeerConnection
import java.util.ArrayList

/**
 * Created by jane on 2017/10/16.
 */
class RoomPresenter : ConferenceClient.ConferenceClientObserver {
    lateinit var roomView: IRoomView
    lateinit var client: ConferenceClient

    var rootEglBase: EglBase? = null
    lateinit var roomThread: HandlerThread
    lateinit var roomHandler: RoomHandler

    //view
    lateinit var remoteStreamRenderer: WoogeenSurfaceRenderer

    class RoomHandler(looper: Looper?) : Handler(looper) {
        override fun handleMessage(msg: Message?) {

        }
    }

    fun initRoom(context: Context) {
        rootEglBase = EglBase.create()
        ClientContext.setApplicationContext(context)
        //To ignore cellular network.
        //ClientContext.addIgnoreNetworkType(ClientContext.NetworkType.CELLULAR);
        ClientContext.setVideoHardwareAccelerationOptions(rootEglBase?.getEglBaseContext(),
                rootEglBase?.getEglBaseContext())
        val config = ConferenceClientConfiguration()
        val iceServers = ArrayList<PeerConnection.IceServer>()
        //iceServers.add(new IceServer(stunAddr));
        //iceServers.add(new IceServer(turnAddrTCP, "woogeen", "master"));
        //iceServers.add(new IceServer(turnAddrUDP, "woogeen", "master"));
        try {
            config.iceServers = iceServers
        } catch (e1: WoogeenException) {
            e1.printStackTrace()
        }

        client = ConferenceClient(config)
        client.addObserver(this)

        roomThread = HandlerThread("Room Thread")
        roomThread.start()
        roomHandler = RoomHandler(roomThread.getLooper())

        initVideoStreamsViews(context)
    }

    private fun initVideoStreamsViews(context: Context) {
//        localStreamRenderer = WoogeenSurfaceRenderer(context)
        remoteStreamRenderer = WoogeenSurfaceRenderer(context)
//        localStreamRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)

//        localViewContainer.addView(localStreamRenderer)
//        remoteViewContainer.addView(remoteStreamRenderer)
        roomView.addRemoteView(remoteStreamRenderer)
//        localStreamRenderer.init(rootEglBase.getEglBaseContext(), null)
        remoteStreamRenderer.init(rootEglBase?.getEglBaseContext(), null)
    }

    fun join(token: String) {
        val connectionOptions = ConnectionOptions()
//        connectionOptions.sslContext = sslContext
//        connectionOptions.hostnameVerifier = hostnameVerifier
    }

    fun leave() {

    }

    fun publish() {

    }

    fun unpublish() {

    }

    //begin for ConferenceClientObserver
    override fun onStreamAdded(p0: RemoteStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUserJoined(p0: User?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onServerDisconnected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessageReceived(p0: String?, p1: String?, p2: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamRemoved(p0: RemoteStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUserLeft(p0: User?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamError(p0: Stream?, p1: WoogeenException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //end for ConferenceClientObserver
}