package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.RoomPresenter
import com.txt.conference.view.IGetUsersView
import com.txt.conference.view.IRoomView
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_add_attendee.*
import kotlinx.android.synthetic.main.layout_attendee.*
import kotlinx.android.synthetic.main.layout_control.*
import org.webrtc.RendererCommon
import java.lang.Exception

/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity(), View.OnClickListener, IRoomView, IGetUsersView {
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

    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        var room: RoomBean = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null) {
            this.finish()
            return
        }

        initGestureDetector()
        initViewEvent()
        roomPresenter = RoomPresenter(this)
        roomPresenter.initRoomInfo(room)
    }

    override fun onResume() {
        super.onResume()
        startHideAllViewDelayed()
    }

    override fun onPause() {
        super.onPause()
        handler.removeMessages(MSG_HIDE_ALL)
    }

    override fun onDestroy() {
        super.onDestroy()
        roomPresenter?.cancelCountDown()
    }

    //for roomPresenter begin
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

    fun initVideoStreamsViews() {

    }
}