package com.txt.conference.presenter

import android.os.CountDownTimer
import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.RoomBean
import com.txt.conference.model.DeleteRoomUserModel
import com.txt.conference.model.IBaseModel
import com.txt.conference.model.IDeleteRoomUserModel
import com.txt.conference.model.Status
import com.txt.conference.view.IDeleteRoomUserView
import com.txt.conference.view.IRoomView
import java.util.*

/**
 * Created by jane on 2017/10/17.
 */
class RoomPresenter {
    lateinit var roomView: IRoomView
    var countDownTimer: CountDownTimer? = null
    val TAG = "RoomPresenter"

    val showConfirmTime = 20000   // 20 min

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

                if (p0 < showConfirmTime && !(showedConfirmDialog!!)){
                    showedConfirmDialog = true
                    roomView.showExtendConfirm()
                }

            }
        }.start()
    }

    fun cancelCountDown() {
        countDownTimer?.cancel()
        countDownTimer=null
    }


    var deleteModel: IDeleteRoomUserModel? = null

    fun InitModel() {
        deleteModel = DeleteRoomUserModel()
    }

    fun deleteRoomUser(room: RoomBean, uid: String, token: String?) {
        if (token == null || token.equals("")) {
            //roomView?.jumpToLogin()
            return
        }
        //deleteView?.showLoading(R.string.deleteing_room)
        deleteModel?.deleteRoomUser(room, uid,token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                //deleteView?.hideLoading()
                when (deleteModel!!.status) {
                    Status.SUCCESS -> {  roomView.updateRoomBean(room) }
                    Status.FAILED -> {}
                    Status.FAILED_TOKEN_AUTH -> {
                        //roomView?.showToast(R.string.error_re_login)
                        //roomView?.jumpToLogin()
                    }
                    //Status.FAILED_UNKNOW -> deleteView?.showToast(R.string.metting_delete_user_error_unknow)
                }
            }
        })
    }
}