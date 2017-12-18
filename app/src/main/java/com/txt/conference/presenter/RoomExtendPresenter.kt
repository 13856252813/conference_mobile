package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.bean.RoomBean
import com.txt.conference.model.*
import com.txt.conference.view.IGetRoomInfoView
import com.txt.conference.view.IGetRoomsView
import com.txt.conference.view.IRoomExtendView

/**
 * Created by pc on 2017/12/12.
 */
class RoomExtendPresenter {
    var roomExtendView: IRoomExtendView? = null
    var roomExtendModel: IRoomExtendModel? = null

    constructor(view: IRoomExtendView) {
        roomExtendView = view
        roomExtendModel = RoomExtendModel()
    }

    fun roomExtend(min: Int, room: RoomBean, token: String?) {
        if (token == null || token.equals("")){
            //roomExtendView?.jumpToLogin()
        } else if (room == null || room!!.roomNo.equals("")){
            roomExtendView?.showToast(R.string.roomno_empty)
        } else {
            //roomExtendView?.showLoading(R.string.geting_room_info)
            roomExtendModel?.roomExtend(min, room, token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    //roomExtendView?.hideLoading()
                    when (roomExtendModel!!.status) {
                        Status.SUCCESS -> {
                            roomExtendView?.showToast(R.string.metting_extend_success)
                            roomExtendView?.extendFinished(room)
                        }
                        Status.FAILED -> { roomExtendView?.extendFailed() }
                        Status.FAILED_TOKEN_AUTH -> { roomExtendView?.showToast(R.string.error_re_login) }
                        Status.FAILED_UNKNOW -> { roomExtendView?.extendFailed() }
                    }
                }
            })
        }
    }
}