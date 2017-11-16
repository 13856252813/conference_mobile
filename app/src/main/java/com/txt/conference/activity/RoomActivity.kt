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
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference_common.WoogeenSurfaceRenderer
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_add_attendee.*
import kotlinx.android.synthetic.main.layout_attendee.*
import kotlinx.android.synthetic.main.layout_control.*
import pub.devrel.easypermissions.EasyPermissions
import android.bluetooth.BluetoothHeadset
import android.content.IntentFilter
import android.net.Uri
import com.common.utlis.DateUtils
import com.txt.conference.adapter.AddTypeAdapter
import com.txt.conference.bean.AddTypeBean
import com.txt.conference.presenter.*
import com.txt.conference.utils.StatusBarUtil
import com.txt.conference.view.*
import com.txt.conference.widget.CustomDialog
import kotlinx.android.synthetic.main.layout_add_attendee_list.*


/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity(), View.OnClickListener, IRoomView, IClientView, IGetUsersView, IInviteUsersView, IGetAddTypeView {




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
    lateinit var clientPresenter: ClientPresenter
    lateinit var getUsersPresenter: GetUsersPresenter
    lateinit var getUserDevicePresenter: GetUserDevicePresenter
    lateinit var inviteUsersPresenter: InviteUsersPresenter
    lateinit var addTypePresenter: AddTypePresenter
    var room: RoomBean? = null
    var attendeeAdapter: AttendeeAdapter? = null
    var inviteAdapter: InviteAdapter? = null
    var addTypeAdapter: AddTypeAdapter? = null
    var mClickedItem = 0
    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }


    private val headsetPlugReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_HEADSET_PLUG) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        room_iv_loud.isEnabled=true;
                        //Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show()
                        if (clientPresenter.getIsSpeakerLoad()){

                        } else {
                            clientPresenter?.onOffLoud()
                        }

                    } else if (intent.getIntExtra("state", 0) == 1) {
                        //Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show()
                        room_iv_loud.isEnabled=false;
                        if (clientPresenter.getIsSpeakerLoad()){
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
        room = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null || intent.getStringExtra(KEY_CONNECT_TOKEN) == null) {
            this.finish()
            return
        }

        initGestureDetector()
        initViewEvent()
        roomPresenter = RoomPresenter(this)
        roomPresenter.initRoomInfo(room!!)
        clientPresenter = ClientPresenter(this, this, room)
        getUsersPresenter = GetUsersPresenter(this)
        getUserDevicePresenter = GetUserDevicePresenter(this)
        addTypePresenter = AddTypePresenter(this)
        inviteUsersPresenter = InviteUsersPresenter(this)

        methodRequiresTwoPermission()
    }

    override fun onResume() {
        super.onResume()
        startHideAllViewDelayed()
        clientPresenter.onResume()
    }

    override fun onPause() {
        clientPresenter?.onPause()
        super.onPause()
        handler.removeMessages(MSG_HIDE_ALL)
    }

    override fun onDestroy() {
        roomPresenter?.cancelCountDown()
        clientPresenter.onDestroy()
        unregisterHeadsetPlugReceiver()
        ULog.d(TAG, "onDestroy")
        super.onDestroy()
    }

    private fun methodRequiresTwoPermission() {
        var args = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *args)) {
            clientPresenter.joinRoom(getConnectToken())
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera), 100, *args)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        clientPresenter.joinRoom(getConnectToken())
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        finish()
    }

    override fun setAttendeeNumber(number: Int) {
        ULog.d(TAG, "setAttendeeNumber $number")
        if (mClickedItem == 0) {
            room_add_attendee_tv_number.text = (getUsersPresenter?.getInvitedUserSize() + number).toString()
        } else if (mClickedItem == 1) {
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

    override fun updateUsers(users: List<AttendeeBean>) {
        runOnUiThread {
            if (attendeeAdapter == null) {
                attendeeAdapter = AttendeeAdapter(R.layout.item_attendee, users)
                attendeeAdapter?.selfName = TxSharedPreferencesFactory(applicationContext).getUserName()
                attendeeAdapter?.creatorName = room?.creator?.display
                initRecyclerView()
                room_attendee_recyclerView.adapter = attendeeAdapter
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
        clientPresenter.finishMeet()
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
        /*var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.roomNo)
        intent.putExtra("sms_body", str_sms_Message)
        startActivity(intent)*/
        var date= DateUtils()
        ULog.i(TAG, "startSendSms" )
        var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.creator?.display,
                date.format(room?.start,DateUtils.HH_mm), room?.roomNo)
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
                    inviteUsersPresenter?.changeInviteList(inviteBean)
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
            clientPresenter.finishMeet()
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
        handler.sendEmptyMessageDelayed(MSG_HIDE_ALL, 1000 * 60)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gesture.onTouchEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            CustomDialog.showSelectDialog(this,resources.getString(R.string.tip_quit_meet),
                    object :com.txt.conference.widget.CustomDialog.DialogClickListener{
                        override fun confirm() {
                            clientPresenter.finishMeet()
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