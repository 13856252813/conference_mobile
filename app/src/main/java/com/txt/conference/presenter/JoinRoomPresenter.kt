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
                if (joinModel?.status == Status.SUCCESS) {
                    joinView?.jumpToRoom(room, joinModel?.token?.token!!)
                } else {
                    joinView?.showError(R.string.enter_room_failed)
                }
            }
        })
    }
}