package com.txt.conference.presenter

import android.os.CountDownTimer
import com.common.utlis.DateUtils
import com.txt.conference.bean.RoomBean
import com.txt.conference.view.IRoomView
import java.util.*

/**
 * Created by jane on 2017/10/17.
 */
class RoomPresenter {
    lateinit var roomView: IRoomView
    lateinit var countDownTimer: CountDownTimer

    constructor(view: IRoomView) {
        roomView = view
    }

    fun initRoomInfo(room: RoomBean) {
        roomView.setRoomNumber(room.roomNo!!)
        var coutDownTime = room.start + room.getDurationMillis() - Date().time
        countDownTimer = object : CountDownTimer(coutDownTime, 1000){
            override fun onFinish() {
                roomView.end()
            }

            override fun onTick(p0: Long) {
                var time = DateUtils().format(Date().time - room.start - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss)
                roomView.setDurationTime(time)
            }
        }.start()
    }
}