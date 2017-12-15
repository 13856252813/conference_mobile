package com.txt.conference.presenter

import android.os.CountDownTimer
import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.bean.RoomBean
import com.txt.conference.view.IRoomView
import java.util.*

/**
 * Created by jane on 2017/10/17.
 */
class RoomPresenter {
    lateinit var roomView: IRoomView
    var countDownTimer: CountDownTimer? = null
    val TAG = "RoomPresenter"
<<<<<<< a13481ee55a864c27e0ac86581aa6f6af64c1787
    val showConfirmTime = 20000   // 20 min
=======

>>>>>>>  * modify for 3.5 new interface.
    var showedConfirmDialog: Boolean? = null

    constructor(view: IRoomView) {
        roomView = view
    }

    fun initRoomInfo(room: RoomBean) {
        showedConfirmDialog = false
        roomView.setRoomNumber(room.roomNo!!)
        roomView.setAllAttendees((room.participants?.size!! + 1).toString())
        roomView.setInviteAbility(room.creator?.uid.equals(roomView?.getCurrentUid()))
        var coutDownTime = room.start + room.getDurationMillis() + room.getDelaytimeMillis() - Date().time
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
        countDownTimer = object : CountDownTimer(coutDownTime, 1000){
            override fun onFinish() {
                roomView.end()
                showedConfirmDialog = false
            }

            override fun onTick(p0: Long) {
                var time = DateUtils().format(Date().time - room.start - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss)
                ULog.i(TAG, "" + coutDownTime.toString())
                ULog.i(TAG, "" + p0)
                ULog.i(TAG, "" + DateUtils().format(room.start + room.getDurationMillis() + room.getDelaytimeMillis() - Date().time - TimeZone.getDefault().rawOffset - 20000, DateUtils.HH_mm_ss))
                ULog.i(TAG, "" + DateUtils().format(room.start + room.getDurationMillis() + room.getDelaytimeMillis() - Date().time - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss))
                roomView.setDurationTime(time)
<<<<<<< a13481ee55a864c27e0ac86581aa6f6af64c1787
                if (p0 < showConfirmTime && !(showedConfirmDialog!!)){
                    showedConfirmDialog = true
                    roomView.showExtendConfirm()
                }
=======
                /*if (p0 < 20000 && !(showedConfirmDialog!!)){
                    showedConfirmDialog = true
                    roomView.showExtendConfirm()
                }*/
>>>>>>>  * modify for 3.5 new interface.
            }
        }.start()
    }

    fun cancelCountDown() {
        countDownTimer?.cancel()
        countDownTimer=null
    }
}