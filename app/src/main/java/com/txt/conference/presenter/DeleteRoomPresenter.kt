package com.txt.conference.presenter

import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.DeleteRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.model.*
import com.txt.conference.view.IDeleteRoomView

/**
 * Created by pc on 2017/11/02.
 */
class DeleteRoomPresenter {
    var TAG = DeleteRoomPresenter::class.java.simpleName
    var deleteModel: IDeleteRoomModel? = null
    var deleteView: IDeleteRoomView? = null

    constructor(view: IDeleteRoomView) {
        deleteView = view
        deleteModel = DeleteRoomModel()
    }

    fun deleteRoom(room: RoomBean, token: String?) {
        if (token == null || token.equals("")) {
            deleteView?.jumpToLogin()
            return
        }
        deleteView?.showLoading(R.string.entering_room)
        deleteModel?.deleteRoom(room, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                deleteView?.hideLoading()
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