package com.txt.conference.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.AttendeeAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.ClientPresenter
import com.txt.conference.presenter.RoomPresenter
import com.txt.conference.view.IClientView
import com.txt.conference.view.IGetUsersView
import com.txt.conference.view.IRoomView
import com.txt.conference_common.WoogeenSurfaceRenderer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_add_attendee.*
import kotlinx.android.synthetic.main.layout_attendee.*
import kotlinx.android.synthetic.main.layout_control.*
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity(), View.OnClickListener, IRoomView, IClientView, IGetUsersView {
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
    var attendeeAdapter: AttendeeAdapter? = null

    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        var room: RoomBean = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null || intent.getStringExtra(KEY_CONNECT_TOKEN) == null) {
            this.finish()
            return
        }

        initGestureDetector()
        initViewEvent()
        roomPresenter = RoomPresenter(this)
        roomPresenter.initRoomInfo(room)
        clientPresenter = ClientPresenter(this, this)

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

    fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        room_attendee_recyclerView.layoutManager = layoutManager
        room_attendee_recyclerView.addItemDecoration(RecyclerViewDivider(this))
    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    //for clientPresenter begin
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
        room_attendee_tv_already_number.setText(number)
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

    }
    //for roomPresenter end

    //for getUsersView begin
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun jumpActivity() {

    }

    override fun addAttendees(conference: List<AttendeeBean>?) {

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

            }
            room_add_attendee_tv_cancel.id -> {

            }
            room_add_attendee_tv_confirm.id -> {

            }
            room_iv_camera.id -> {

            }
            room_iv_mute.id -> {

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
        handler.sendEmptyMessageDelayed(MSG_HIDE_ALL, 1000 * 5)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gesture.onTouchEvent(event)
    }
}