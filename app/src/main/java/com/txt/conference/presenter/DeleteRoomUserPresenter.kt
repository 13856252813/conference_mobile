package com.txt.conference.presenter

import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.DeleteRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.model.*
import com.txt.conference.view.IDeleteRoomUserView
import com.txt.conference.view.IDeleteRoomView

/**
 * Created by pc on 2017/12/13.
 */
class DeleteRoomUserPresenter {
    var TAG = DeleteRoomUserPresenter::class.java.simpleName
    var deleteModel: IDeleteRoomUserModel? = null
    var deleteView: IDeleteRoomUserView? = null

    constructor(view: IDeleteRoomUserView) {
        deleteView = view
        deleteModel = DeleteRoomUserModel()
    }

    fun deleteRoom(room: RoomBean, token: String?) {
        if (token == null || token.equals("")) {
            deleteView?.jumpToLogin()
            return
        }
        //deleteView?.showLoading(R.string.deleteing_room)
        deleteModel?.deleteRoomUser(room, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                //deleteView?.hideLoading()
                when (deleteModel!!.status) {
                    Status.SUCCESS -> deleteView?.deleteFinished()
                    Status.FAILED -> deleteView?.showToast(deleteModel?.msg!!)
                    Status.FAILED_TOKEN_AUTH -> {
                        deleteView?.showToast(R.string.error_re_login)
                        deleteView?.jumpToLogin()
                    }
                    Status.FAILED_UNKNOW -> deleteView?.showToast(R.string.error_unknow)
                }
            }
        })
    }
}