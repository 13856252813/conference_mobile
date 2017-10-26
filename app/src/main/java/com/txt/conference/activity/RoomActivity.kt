package com.txt.conference.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.common.utlis.ULog
import com.common.widget.LoadingView
import com.txt.conference.R
import com.txt.conference.adapter.AttendeeAdapter
import com.txt.conference.adapter.InviteAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.ClientPresenter
import com.txt.conference.presenter.GetUsersPresenter
import com.txt.conference.presenter.InviteUsersPresenter
import com.txt.conference.presenter.RoomPresenter
import com.txt.conference.view.IClientView
import com.txt.conference.view.IGetUsersView
import com.txt.conference.view.IInviteUsersView
import com.txt.conference.view.IRoomView
import com.txt.conference_common.WoogeenSurfaceRenderer
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_add_attendee.*
import kotlinx.android.synthetic.main.layout_attendee.*
import kotlinx.android.synthetic.main.layout_control.*
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity(), View.OnClickListener, IRoomView, IClientView, IGetUsersView, IInviteUsersView {
    val TAG = RoomActivity::class.java.simpleName
    lateinit var gesture: GestureDetector

    val MSG_HIDE_ALL = 1
    var handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_HIDE_ALL -> {
                    if (room_layout_attendee_container.visibility == View.VISIBLE) {
                        room_layout_attendee_container.visibility = View.INVISIBLE
                    }

                    if (room_layout_control.visibility == View.VISIBLE) {
                        room_layout_control.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    lateinit var roomPresenter: RoomPresenter
    lateinit var clientPresenter: ClientPresenter
    lateinit var getUsersPresenter: GetUsersPresenter
    lateinit var inviteUsersPresenter: InviteUsersPresenter
    var room: RoomBean? = null
    var attendeeAdapter: AttendeeAdapter? = null
    var inviteAdapter: InviteAdapter? = null

    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        room = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null || intent.getStringExtra(KEY_CONNECT_TOKEN) == null) {
            this.finish()
            return
        }

        initGestureDetector()
        initViewEvent()
        roomPresenter = RoomPresenter(this)
        roomPresenter.initRoomInfo(room!!)
        clientPresenter = ClientPresenter(this, this)
        getUsersPresenter = GetUsersPresenter(this)
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
        super.onDestroy()
        roomPresenter?.cancelCountDown()
        clientPresenter.dettach()
        ULog.d(TAG, "onDestroy")
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
        room_add_attendee_tv_number.setText((getUsersPresenter?.getInvitedUserSize() + number).toString())
    }

    override fun setAttendeeAllNumber(number: Int) {
        room_add_attendee_tv_all_number.setText(number.toString())
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

    override fun isMicrophoneMute(isMicrophoneMute: Boolean) {
        if (isMicrophoneMute) room_iv_mute.setImageResource(R.mipmap.muted) else room_iv_mute.setImageResource(R.mipmap.mute)
    }

    override fun updateUsers(users: List<AttendeeBean>) {
        runOnUiThread {
            if (attendeeAdapter == null) {
                attendeeAdapter = AttendeeAdapter(R.layout.item_attendee, users)
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

    override fun setAllAttendees(number: String) {
        room_attendee_tv_all_number.setText(number)
    }

    override fun setRoomNumber(number: String) {
        room_tv_number.setText(String.format(getString(R.string.room_number), number))
    }

    override fun setDurationTime(time: String) {
        room_tv_time.setText(time)
    }

    override fun end() {
        showToast(R.string.conference_end)
        this.finish()
    }
    //for roomPresenter end

    //for getUsersView begin
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun getUid(): String? {
        return TxSharedPreferencesFactory(applicationContext).getId()
    }

    override fun jumpActivity() {

    }

    fun showAttendees() {
        if (room_layout_add_attendee.visibility == View.VISIBLE) {
            room_layout_add_attendee.visibility = View.INVISIBLE
        }
        if (room_layout_add_attendee.visibility == View.VISIBLE) {
            room_layout_add_attendee.visibility = View.INVISIBLE
        }
        if (room_layout_attendee.visibility != View.VISIBLE) {
            room_layout_attendee.visibility = View.VISIBLE
        }
    }

    fun showAddAttendees() {
        if (room_layout_attendee_container.visibility != View.VISIBLE) {
            room_layout_attendee_container.visibility = View.VISIBLE
        }
        if (room_layout_attendee.visibility == View.VISIBLE) {
            room_layout_attendee.visibility = View.INVISIBLE
        }
        if (room_layout_add_attendee.visibility != View.VISIBLE) {
            room_layout_add_attendee.visibility = View.VISIBLE
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
            this.finish()
        }

        room_iv_attendee.setOnClickListener(this)
        room_add_attendee_tv_cancel.setOnClickListener(this)
        room_add_attendee_tv_confirm.setOnClickListener(this)
        room_attendee_iv_add.setOnClickListener(this)

        room_iv_camera.setOnClickListener(this)
        room_iv_mute.setOnClickListener(this)
        room_iv_share.setOnClickListener(this)
        room_iv_turn.setOnClickListener(this)
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
                getUsersPresenter?.getUsers(getToken(), getInviteAttendees())
            }
            room_add_attendee_tv_cancel.id -> {
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
        }
    }

    private fun initGestureDetector() {
        gesture = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(p0: MotionEvent?) {

            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                if (room_layout_attendee_container.visibility == View.VISIBLE) {
                    room_layout_attendee_container.visibility = View.INVISIBLE
                    startHideAllViewDelayed()
                    return false
                }
                if (room_layout_control.visibility != View.VISIBLE) {
                    room_layout_control.visibility = View.VISIBLE
                    startHideAllViewDelayed()
                } else {
                    room_layout_control.visibility = View.INVISIBLE
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
}