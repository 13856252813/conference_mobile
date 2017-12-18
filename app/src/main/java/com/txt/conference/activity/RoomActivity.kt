package com.txt.conference.activity

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.AttendeeAdapter
import com.txt.conference.adapter.InviteAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference_common.WoogeenSurfaceRenderer
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_add_attendee.*
import kotlinx.android.synthetic.main.layout_attendee.*
import kotlinx.android.synthetic.main.layout_control.*
import pub.devrel.easypermissions.EasyPermissions
import android.bluetooth.BluetoothHeadset
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import com.common.utlis.DateUtils
import com.tofu.conference.widget.ScreenDialog
import com.txt.conference.adapter.AddTypeAdapter
import com.txt.conference.bean.*
import com.txt.conference.event.MessageEvent
import com.txt.conference.http.Urls
import com.txt.conference.model.MutToRoomBean
import com.txt.conference.presenter.*
import com.txt.conference.utils.CommonUtils
import com.txt.conference.utils.Constants
import com.txt.conference.utils.CustomExtendDialog
import com.txt.conference.utils.StatusBarUtil
import com.txt.conference.utils.ToastUtils
import com.txt.conference.utils.*
import kotlinx.android.synthetic.main.item_attendee.*
import com.txt.conference.view.*
import com.txt.conference.widget.CustomDialog
import kotlinx.android.synthetic.main.item_attendee.*
import kotlinx.android.synthetic.main.layout_add_attendee_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity(), View.OnClickListener, IRoomView, IRoomExtendView, IClientView, IGetUsersView, IInviteUsersView, IGetAddTypeView {

    override fun extendFailed() {
        showExtendFailedDialog()
    }

    override fun extendFinished(roomBean: RoomBean) {
        room = roomBean
        runOnUiThread {
            roomPresenter?.cancelCountDown()
            roomPresenter?.initRoomInfo(room!!)

            showExtendConfirm = false
        }

    }


    override fun onJoined() {
        headsetType = isWiredHeadsetOn()//DeviceUtils.isHeadsetExists()
        ULog.i(TAG, "headsetType:" + headsetType)
        if (headsetType){
            clientPresenter?.onOffLoud()
        }
    }


    fun isWiredHeadsetOn() :Boolean {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.isWiredHeadsetOn
    }

    val TAG = RoomActivity::class.java.simpleName
    lateinit var gesture: GestureDetector

    val MSG_HIDE_ALL = 1
    var handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_HIDE_ALL -> {
                    if (room_layout_attendee_container.visibility == View.VISIBLE) {
                        room_layout_attendee_container.visibility = View.GONE
                    }

                    if (room_layout_control.visibility == View.VISIBLE) {
                        room_layout_control.visibility = View.GONE
                    }
                }
            }
        }
    }

    lateinit var roomPresenter: RoomPresenter
    var clientPresenter: ClientPresenter? = null
    lateinit var getUsersPresenter: GetUsersPresenter
    lateinit var getUserDevicePresenter: GetUserDevicePresenter
    lateinit var inviteUsersPresenter: InviteUsersPresenter
    lateinit var addTypePresenter: AddTypePresenter
    lateinit var roomExtendPresenter: RoomExtendPresenter  
    var room: RoomBean? = null
    var attendeeAdapter: AttendeeAdapter? = null
    var inviteAdapter: InviteAdapter? = null
    var addTypeAdapter: AddTypeAdapter? = null
    var mClickedItem = 0
    var headsetType = false

    var mContext:Context? = null
    var attendType = 0
    var showExtendConfirm = false
    var deleteUserId = ""

    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
        var MIN_10 = 10
        var MIN_30 = 30
        var MIN_60 = 60
    }


    private val headsetPlugReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_HEADSET_PLUG) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        room_iv_loud.isEnabled=true;
                        //Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show()
                        if (clientPresenter!!.getIsSpeakerLoad()){

                        } else {
                            clientPresenter?.onOffLoud()
                        }

                    } else if (intent.getIntExtra("state", 0) == 1) {
                        //Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show()
                        room_iv_loud.isEnabled=false;
                        if (clientPresenter!!.getIsSpeakerLoad()){
                            clientPresenter?.onOffLoud()
                        }

                    }
                }
            }
        }
    }

    private fun registerHeadsetPlugReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.HEADSET_PLUG")
        registerReceiver(headsetPlugReceiver, intentFilter)

        // for bluetooth headset connection receiver
        val bluetoothFilter = IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        registerReceiver(headsetPlugReceiver, bluetoothFilter)
    }

    private fun unregisterHeadsetPlugReceiver() {
        unregisterReceiver(headsetPlugReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        registerHeadsetPlugReceiver()
        ULog.i(TAG, "onCreate")
        mContext = this
        room = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null || intent.getStringExtra(KEY_CONNECT_TOKEN) == null) {
            this.finish()
            return
        }

        initGestureDetector()
        initViewEvent()
        roomPresenter = RoomPresenter(this)
        roomPresenter.initRoomInfo(room!!)
        roomPresenter?.InitModel()
        clientPresenter = ClientPresenter(this, this, room)
        getUsersPresenter = GetUsersPresenter(this)
        getUserDevicePresenter = GetUserDevicePresenter(this)
        addTypePresenter = AddTypePresenter(this)
        inviteUsersPresenter = InviteUsersPresenter(this)
        methodRequiresTwoPermission()

        EventBus.getDefault().register(this)
        roomExtendPresenter = RoomExtendPresenter(this)
	    showExtendConfirm = false
	    //showExtendConfirm()
    }

    override fun onResume() {
        super.onResume()
        startHideAllViewDelayed()
        clientPresenter!!.onResume()
    }

    override fun onStop() {
        super.onStop()
        clientPresenter?.onStop()
    }

    override fun onPause() {
        clientPresenter?.onPause()
        super.onPause()
        handler.removeMessages(MSG_HIDE_ALL)
    }

    override fun onDestroy() {
        roomPresenter?.cancelCountDown()
        clientPresenter!!.onDestroy()
        clientPresenter = null
        unregisterHeadsetPlugReceiver()
        ULog.d(TAG, "onDestroy")
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe
    fun onEventMainThread(event: MessageEvent) {
        ULog.d(TAG, "onEventMainThread")
        if(event.eventCode == MessageEvent.MUTETOUSERS){
            var mMutRoomBean=event.getDataObject(MutToRoomBean::class.java)
            var list=mMutRoomBean.room.participants
            for (bean in list){
                if(bean.id==mMutRoomBean.uid){
                    ToastUtils.topShow("${bean.name}摄像头被关闭")
                }
            }
        } else if(event.eventCode == MessageEvent.MUTEUSER){
            var mMutRoomBean=event.getDataObject(CreateConferenceRoomBean::class.java)
            var list=mMutRoomBean.data!!.participants
            for (bean in list!!){
                if(bean.id == getCurrentUid()){
                    if (bean.audioMute == 0){
                        ToastUtils.topShow("${bean.name}audioMute 0")
                    } else {
                        ToastUtils.topShow("${bean.name}audioMute 1")
                    }
                    if (bean.videoMute == 0){
                        ToastUtils.topShow("${bean.name}videoMute 0")
                    } else {
                        ToastUtils.topShow("${bean.name}videoMute 1")
                    }
                    ToastUtils.topShow("${bean.name}摄像头被关闭")
                }
            }
        } else if(event.eventCode == MessageEvent.DELETEROOMUSER){
            var mDeleteRoomBean=event.getDataObject(DeleteRoomUserBean::class.java)
            if (mDeleteRoomBean.deleteuid.equals(getCurrentUid())){
                clientPresenter?.finishMeet()
            }
            ToastUtils.topShow(getString(R.string.metting_room_delete_useruser_message))
        }
    }


    private fun methodRequiresTwoPermission() {
        var args = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *args)) {
            clientPresenter?.joinRoom(getConnectToken())
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera), 100, *args)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        clientPresenter?.joinRoom(getConnectToken())
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        finish()
    }

    override fun setAttendeeNumber(number: Int) {
        ULog.d(TAG, "setAttendeeNumber $number")
        if (mClickedItem == 0) {
            attendType = MainActivity.ATTEND_TYPE_ACCOUNT
            room_add_attendee_tv_number.text = (getUsersPresenter?.getInvitedUserSize() + number).toString()
        } else if (mClickedItem == 1) {
            attendType = MainActivity.ATTEND_TYPE_DEVICE
            room_add_attendee_tv_number.text = (getUserDevicePresenter?.getInvitedUserSize() + number).toString()
        }
    }

    override fun setAttendeeAllNumber(number: Int) {
        room_add_attendee_tv_all_number.text = number.toString()
    }

    fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        room_attendee_recyclerView.layoutManager = layoutManager
        room_attendee_recyclerView.addItemDecoration(RecyclerViewDivider(this, R.drawable.invite_divider))
    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    //for inviteUsersPresenter begin
    override fun inviteComplete(room: RoomBean) {
        this.room = room
        roomPresenter.initRoomInfo(this.room!!)
        clientPresenter?.updateRoomBean(room)
        showAttendees()
        showToast(R.string.tips_invite_success)
        startHideAllViewDelayed()
    }

    override fun getRoomId(): String? {
        return room?.roomId
    }
    //for inviteUsersPresenter end

    //for clientPresenter begin
    override fun onOffCamera(isOpenCamera: Boolean) {
        if (isOpenCamera) {
            runOnUiThread { room_iv_camera.setImageResource(R.mipmap.camera_open) }
        } else {
            runOnUiThread { room_iv_camera.setImageResource(R.mipmap.camera_closed) }
        }
    }

    override fun onOffLoud(isOpenLoud: Boolean) {
        if (isOpenLoud) {
            runOnUiThread { room_iv_loud.setImageResource(R.mipmap.loud) }
        } else {
            runOnUiThread { room_iv_loud.setImageResource(R.mipmap.loud_off) }
        }
    }

    override fun isMicrophoneMute(isMicrophoneMute: Boolean) {
        if (isMicrophoneMute) room_iv_mute.setImageResource(R.mipmap.muted) else room_iv_mute.setImageResource(R.mipmap.mute)
    }

    fun GetMuteVideoType(uid: String): Int?{
        for (i in 0..room!!.participants!!.size - 1) {
            if (room!!.participants!![i].id!!.equals(uid)){
                return room!!.participants!![i].videoMute
            }
        }
        return 0
    }

    fun GetMuteVoiceType(uid: String): Int?{
        for (i in 0..room!!.participants!!.size - 1) {
            if (room!!.participants!![i].id!!.equals(uid)){
                return room!!.participants!![i].audioMute
            }
        }
        return 0
    }

    fun CompchangedVideo(uid: String){
        var muteType = GetMuteVideoType(uid)
        clientPresenter?.sendMediaStatus(getRoomId(), uid, ClientPresenter.VEDIO_MUTE, muteType.toString(), ClientPresenter.ACTION_COMP
                , getToken())
    }

    fun CompchangedVoice(uid: String){
        var muteType = GetMuteVoiceType(uid)
        clientPresenter?.sendMediaStatus(getRoomId(), uid, ClientPresenter.VOICE_MUTE, muteType.toString(), ClientPresenter.ACTION_COMP
                , getToken())
    }

    override fun updateUsers(users: List<AttendeeBean>) {
        runOnUiThread {
            if (attendeeAdapter == null) {
                attendeeAdapter = AttendeeAdapter(R.layout.item_attendee, users)
                attendeeAdapter?.selfName = TxSharedPreferencesFactory(applicationContext).getUserName()
                attendeeAdapter?.creatorName = room?.creator?.display
                initRecyclerView()
                room_attendee_recyclerView.adapter = attendeeAdapter

                attendeeAdapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                    var userBean = adapter?.data?.get(position) as AttendeeBean
                    deleteUserId = userBean.id!!
                    when(view?.id) {
                        item_attendee_iv_vedio.id -> {
                            ULog.d(TAG, "onItemChildClick $position vedio:" + userBean.id)
                            CompchangedVideo(deleteUserId)
                        }
                        item_attendee_iv_sound.id -> {
                            ULog.d(TAG, "onItemChildClick $position sound:" + userBean.id)
                            CompchangedVoice(deleteUserId)
                        }
                        item_attendee_iv_more.id -> {
                            ULog.d(TAG, "onItemChildClick $position more:" + userBean.id)
                            showDeleteUserDialog()
                        }
                    }
                }
            } else {
                attendeeAdapter?.setNewData(users)
            }
        }
    }

    override fun setAlreadyAttendees(number: String) {
        runOnUiThread { room_attendee_tv_already_number.setText(number) }
    }

    override fun getConnectToken(): String {
        return intent.getStringExtra(KEY_CONNECT_TOKEN)
    }

    override fun addRemoteView(remoteView: WoogeenSurfaceRenderer) {
        room_remote_container.addView(remoteView)
    }

    override fun switchCamera(isFrontCamera: Boolean) {
        runOnUiThread {
            toast(if (isFrontCamera) "已转换到前置摄像头" else "已转换到后置摄像头")
        }
    }
    //for clientPresenter end

    //for roomPresenter begin
    override fun getInviteAttendees(): List<ParticipantBean> {
        return room!!.participants!!
    }

    override fun setInviteAbility(ability: Boolean) {
        room_attendee_iv_add.visibility = if (ability) View.VISIBLE else View.GONE
    }

    override fun getCurrentUid(): String {
        return TxSharedPreferencesFactory(applicationContext).getId()!!
    }

    override fun setAllAttendees(number: String) {
        room_attendee_tv_all_number.text = number
    }

    override fun setRoomNumber(number: String) {
        room_tv_number.text = String.format(getString(R.string.room_number), number)
    }

    override fun setDurationTime(time: String) {
        room_tv_time.text = time
    }

    override fun end() {
        showToast(R.string.conference_end)
        clientPresenter?.finishMeet()
    }

    fun StartDeleteUser(strId: String ){
        //deleteRoomUserPresenter.deleteRoomUser(room!!, )
        roomPresenter?.deleteRoomUser(room!!, strId, getToken())
    }


    fun showDeleteUserDialog(){
        val builder = CustomDeleteUserDialog.Builder(this)
        builder.setDeleteUserButton(){
            dialog, _ -> dialog.dismiss()
            StartDeleteUser(deleteUserId)
        }
        builder.setCancelButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.create().show()
    }

    fun startExtend(min: Int){
        roomExtendPresenter?.roomExtend(min, room!!, getToken())
    }

    fun showChoosExtendTimeDialog(){
        val builder = CustomExtendDialog.Builder(this)
        builder.set10mButton{
            dialog, _ -> dialog.dismiss()
            startExtend(MIN_10)
        }
        builder.set30mButton{
            dialog, _ -> dialog.dismiss()
            startExtend(MIN_30)
        }
        builder.set1HButton{
            dialog, _ -> dialog.dismiss()
            startExtend(MIN_60)
        }
        builder.setCancelButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.create().show()
    }

    fun showExtendFailedDialog(){

        CustomDialog.showCommonDialog(this,
                resources.getString(R.string.metting_extend_failed_title),
                resources.getString(R.string.metting_extend_failed_message),
                resources.getString(R.string.metting_extend_failed_retry),
                resources.getString(R.string.metting_end_confirm_skip),
                object : com.txt.conference.widget.CustomDialog.DialogClickListener {
                    override fun confirm() {
                        //jumpFaceLogin()
                        showChoosExtendTimeDialog()
                    }

                    override fun cancel() {

                    }

                }
        )
    }
    override fun showExtendConfirm() {
        if (showExtendConfirm){
            return
        }
        var uid = TxSharedPreferencesFactory(applicationContext).getId()
        if (!(room!!.creator!!.uid!!.equals(uid))){
            return
        }
        showExtendConfirm = true
        CustomDialog.showCommonDialog(this,
                resources.getString(R.string.metting_end_confirm_title),
                "",
                resources.getString(R.string.metting_end_confirm_ok),
                resources.getString(R.string.metting_end_confirm_skip),
                object : com.txt.conference.widget.CustomDialog.DialogClickListener {
                    override fun confirm() {
                        //jumpFaceLogin()
                        showChoosExtendTimeDialog()
                    }

                    override fun cancel() {

                    }

                }
        )
    }
    //for roomPresenter end

    //for getUsersView begin
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun getUid(): String? {
        return room?.creator?.uid
    }

    override fun jumpActivity() {

    }

    private fun showAttendees() {
        if (room_layout_attendee_container.visibility != View.VISIBLE) {
            room_layout_attendee_container.visibility = View.VISIBLE
        }
        if (room_layout_attendee.visibility != View.VISIBLE) {
            room_layout_attendee.visibility = View.VISIBLE
        }
        if (room_layout_add_attendee_list.visibility == View.VISIBLE) {
            room_layout_add_attendee_list.visibility = View.GONE
        }
        if (room_layout_add_attendee.visibility == View.VISIBLE) {
            room_layout_add_attendee.visibility = View.GONE
        }
    }

    private fun showAddAttendees() {
        if (room_layout_attendee_container.visibility != View.VISIBLE) {
            room_layout_attendee_container.visibility = View.VISIBLE
        }
        if (room_layout_attendee.visibility == View.VISIBLE) {
            room_layout_attendee.visibility = View.GONE
        }
        if (room_layout_add_attendee.visibility != View.VISIBLE) {
            room_layout_add_attendee.visibility = View.VISIBLE
        }
        if (room_layout_add_attendee_list.visibility == View.VISIBLE) {
            room_layout_add_attendee_list.visibility = View.GONE
        }
    }

    private fun showAddTypeAttendees() {
        if (room_layout_attendee_container.visibility != View.VISIBLE) {
            room_layout_attendee_container.visibility = View.VISIBLE
        }
        if (room_layout_attendee.visibility == View.VISIBLE) {
            room_layout_attendee.visibility = View.GONE
        }
        if (room_layout_add_attendee_list.visibility != View.VISIBLE) {
            room_layout_add_attendee_list.visibility = View.VISIBLE
        }
        if (room_layout_add_attendee.visibility == View.VISIBLE) {
            room_layout_add_attendee.visibility = View.GONE
        }
    }

    fun startSendSms(){
        ULog.i(TAG, "startSendSms" )
        var date= DateUtils()
        var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.creator?.display,
                date.format(room?.start,DateUtils.HH_mm), room?.roomNo, Urls.HOST)
        intent.putExtra("sms_body", str_sms_Message)
        startActivity(intent)
    }

    override fun initAddTypeViewData(listdata: ArrayList<AddTypeBean>) {
        ULog.i(TAG, listdata?.size!!.toString())
        showAddTypeAttendees()
        if (addTypeAdapter == null){
            addTypeAdapter = AddTypeAdapter(R.layout.item_addtype, listdata)
            addTypeAdapter?.onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    when(position) {
                        0 -> {
                            getUsersPresenter?.getUsers(getToken(), getInviteAttendees())
                        }
                        1 -> {
                            getUserDevicePresenter?.getUsers(getToken(), getInviteAttendees())

                        }
                        2 -> {
                            startSendSms()
                            //CommonUtils.startSendSms(mContext!!, room!!)
                        }
                    }
                    mClickedItem = position
                }
            }
            var layoutManager = LinearLayoutManager(this)
            room_add_class_attendee_recycler.layoutManager = layoutManager
            room_add_class_attendee_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    startHideAllViewDelayed()
                }
            })
            room_add_class_attendee_recycler.addItemDecoration(RecyclerViewDivider(this, R.drawable.invite_divider, 0, 0))
            room_add_class_attendee_recycler.adapter = addTypeAdapter

        }

    }
    override fun addAttendees(users: List<AttendeeBean>?) {
        showAddAttendees()
        if (inviteAdapter == null) {
            inviteAdapter = InviteAdapter(R.layout.item_invite, users)
            inviteAdapter?.onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    var inviteBean = adapter?.getItem(position) as AttendeeBean
                    if (inviteBean.cantchange) {
                        return
                    }
                    inviteBean.invited = !inviteBean.invited

                    //inviteUsersPresenter?.changeInviteList(inviteBean)

                    inviteUsersPresenter?.changeInviteList(attendType, inviteBean)
                    adapter?.notifyItemChanged(position)
                    startHideAllViewDelayed()
                }
            }

            var layoutManager = LinearLayoutManager(this)
            room_add_attendee_recycler.layoutManager = layoutManager
            room_add_attendee_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    startHideAllViewDelayed()
                }
            })
            room_add_attendee_recycler.addItemDecoration(RecyclerViewDivider(this, R.drawable.invite_divider, 0, 0))
            room_add_attendee_recycler.adapter = inviteAdapter
        } else {
            inviteAdapter?.setNewData(users)
        }
        startHideAllViewDelayed()
    }

    override fun back() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }
    //for getUsersView end
    private fun initViewEvent() {
        room_iv_quit.setOnClickListener {
            ULog.d(TAG, "image click")
            clientPresenter?.finishMeet()
        }

        room_iv_attendee.setOnClickListener(this)
        room_add_attendee_tv_cancel.setOnClickListener(this)
        room_add_attendee_tv_confirm.setOnClickListener(this)
        room_attendee_iv_add.setOnClickListener(this)
        room_add_attendee_tv_back.setOnClickListener(this)

        room_iv_camera.setOnClickListener(this)
        room_iv_mute.setOnClickListener(this)
        room_iv_share.setOnClickListener(this)
        room_iv_turn.setOnClickListener(this)
        room_iv_loud.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        startHideAllViewDelayed()
        when(p0?.id) {
            room_iv_attendee.id -> {
                if (room_layout_attendee_container.visibility != View.VISIBLE) {
                    room_layout_attendee_container.visibility = View.VISIBLE
                }
            }
            room_attendee_iv_add.id -> {
                //getUsersPresenter?.getUsers(getToken(), getInviteAttendees())
                addTypePresenter?.initAddTypeViewData()

            }
            room_add_attendee_tv_cancel.id -> {
                showAddTypeAttendees()
            }
            room_add_attendee_tv_back.id -> {
                showAttendees()
            }
            room_add_attendee_tv_confirm.id -> {
                inviteUsersPresenter?.invite(getRoomId(), getToken())
            }
            room_iv_camera.id -> {
                clientPresenter?.onOffcamera()
            }
            room_iv_mute.id -> {
                clientPresenter?.onOffMicrophone()
            }
            room_iv_share.id -> {
               if(ScreenDialog.mInstance!=null && !(ScreenDialog.mInstance?.isShowing)!!){
                   ScreenDialog.mInstance?.show()
               }
            }
            room_iv_turn.id -> {
                clientPresenter?.switchCamera()
            }
            room_iv_loud.id -> {
                clientPresenter?.onOffLoud()
            }
        }
    }

    private fun initGestureDetector() {
        gesture = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(p0: MotionEvent?) {

            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                if (room_layout_attendee_container.visibility == View.VISIBLE) {
                    room_layout_attendee_container.visibility = View.GONE
                    startHideAllViewDelayed()
                    return false
                }
                if (room_layout_control.visibility != View.VISIBLE) {
                    room_layout_control.visibility = View.VISIBLE
                    if(clientPresenter?.getRemoteScreeenStream()!=null) {
                        room_iv_share.visibility = View.VISIBLE
                    }else{
                        room_iv_share.visibility = View.GONE
                    }
                    startHideAllViewDelayed()
                } else {
                    room_layout_control.visibility = View.GONE
                }
                return false
            }

            override fun onDown(p0: MotionEvent?): Boolean {
                return false
            }

            override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                return false
            }

            override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                return false
            }

            override fun onLongPress(p0: MotionEvent?) {

            }

        })
    }



    fun startHideAllViewDelayed() {
        handler.removeMessages(MSG_HIDE_ALL)
        handler.sendEmptyMessageDelayed(MSG_HIDE_ALL, 1000 * 6)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gesture.onTouchEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            CustomDialog.showSelectDialog(this,resources.getString(R.string.tip_quit_meet),
                    object :com.txt.conference.widget.CustomDialog.DialogClickListener{
                        override fun confirm() {
                            clientPresenter?.finishMeet()
                        }
                        override fun cancel() {
                        }

                    })
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparent(this)
    }


}