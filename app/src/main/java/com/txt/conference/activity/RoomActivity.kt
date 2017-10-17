package com.txt.conference.activity

import android.os.Bundle
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.common.utlis.ULog
import com.txt.conference.R
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.layout_control.*
import org.webrtc.RendererCommon

/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity() {
    val TAG = RoomActivity::class.java.simpleName
    lateinit var gesture: GestureDetector

    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        initGestureDetector()
        initViewEvent()
    }

    private fun initViewEvent() {
        room_iv_quit.setOnClickListener {
            ULog.d(TAG, "image click")
            this.finish()
        }

        room_iv_attendee.setOnClickListener {

        }

        room_iv_camera.setOnClickListener {

        }

        room_iv_mute.setOnClickListener {

        }

        room_iv_share.setOnClickListener {

        }

        room_iv_turn.setOnClickListener {

        }
    }

    private fun initGestureDetector() {
        gesture = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(p0: MotionEvent?) {

            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                if (room_layout_control.visibility != View.VISIBLE) {
                    room_layout_control.visibility = View.VISIBLE
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gesture.onTouchEvent(event)
    }

    fun initVideoStreamsViews() {

    }
}