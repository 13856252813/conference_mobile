package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.bean.RoomBean
import com.txt.conference.model.IBaseModel
import com.txt.conference.model.IJoinRoomModel
import com.txt.conference.model.JoinRoomModel
import com.txt.conference.model.Status
import com.txt.conference.view.IJoinRoomView

/**
 * Created by jane on 2017/10/15.
 */
class JoinRoomPresenter {
    var joinModel: IJoinRoomModel? = null
    var joinView: IJoinRoomView? = null

    constructor(view: IJoinRoomView) {
        joinView = view
        joinModel = JoinRoomModel()
    }

    fun joinRoom(room: RoomBean, token: String?) {
        if (token == null || token.equals("")) {
            joinView?.jumpToLogin()
            return
        }
        joinView?.showLoading(R.string.entering_room)
        joinModel?.joinRoom(room.roomId!!, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                joinView?.hideLoading()
                when (joinModel!!.status) {
                    Status.SUCCESS -> joinView?.jumpToRoom(room, joinModel?.token?.token!!)
                    Status.FAILED -> joinView?.showToast(joinModel?.msg!!)
                    Status.FAILED_TOKEN_AUTH -> {
                        joinView?.showToast(R.string.error_re_login)
                        joinView?.jumpToLogin()
                    }
                    Status.FAILED_UNKNOW -> joinView?.showToast(R.string.error_unknow)
                }
            }
        })
    }
}