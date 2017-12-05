package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.IGetRoomInfoView
import com.txt.conference.view.IGetRoomsView

/**
 * Created by jane on 2017/11/17.
 */
class GetRoomInfoPresenter {
    var getRoomInfoView: IGetRoomInfoView? = null
    var getRoomInfoModel: IGetRoomInfoModel? = null

    constructor(view: IGetRoomInfoView) {
        getRoomInfoView = view
        getRoomInfoModel = GetRoomInfoModel()
    }

    fun getRoomInfo(roomNo: String?, token: String?) {
        if (token == null || token.equals("")){
            getRoomInfoView?.jumpToLogin()
        } else if (roomNo == null || roomNo.equals("")){
            getRoomInfoView?.showToast(R.string.roomno_empty)
        } else {
            getRoomInfoView?.showLoading(R.string.geting_room_info)
            getRoomInfoModel?.loadRoomInfo(roomNo, token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    getRoomInfoView?.hideLoading()
                    when (getRoomInfoModel!!.status) {
                        Status.SUCCESS -> { getRoomInfoView?.getRoomInfoFinished(true, getRoomInfoModel?.roominfo) }//getRoomsView?.addConferences(getRoomInfoModel?.rooms)
                        Status.FAILED -> {
                            getRoomInfoView?.showToast(R.string.conference_end)
                            getRoomInfoView?.getRoomInfoFinished(false, getRoomInfoModel?.roominfo)
                        }//getRoomsView?.showToast(getRoomsModel?.msg!!)
                        Status.FAILED_TOKEN_AUTH -> {
                            getRoomInfoView?.showToast(R.string.error_re_login)
                            getRoomInfoView?.jumpToLogin()
                        }
                        Status.FAILED_UNKNOW -> getRoomInfoView?.showToast(R.string.error_unknow)
                    }
                }
            })
        }
    }
}