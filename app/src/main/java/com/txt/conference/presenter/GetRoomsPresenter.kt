package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.GetRoomsModel
import com.txt.conference.model.IBaseModel
import com.txt.conference.model.IGetRoomsModel
import com.txt.conference.model.Status
import com.txt.conference.view.IGetRoomsView

/**
 * Created by jane on 2017/10/12.
 */
class GetRoomsPresenter {
    var getRoomsView: IGetRoomsView? = null
    var getRoomsModel: IGetRoomsModel? = null

    constructor(view: IGetRoomsView) {
        getRoomsView = view
        getRoomsModel = GetRoomsModel()
    }

    fun getRooms(token: String?) {
        if (token == null || token.equals("")){
            getRoomsView?.jumpToLogin()
        } else {
            getRoomsView?.showLoading(R.string.loading_rooms)
            getRoomsModel?.loadRooms(token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    getRoomsView?.hideLoading()
                    when (getRoomsModel!!.status) {
                        Status.SUCCESS -> getRoomsView?.addConferences(getRoomsModel?.rooms)
                        Status.FAILED -> getRoomsView?.showToast(getRoomsModel?.msg!!)
                        Status.FAILED_TOKEN_AUTH -> {
                            getRoomsView?.showToast(R.string.error_re_login)
                            getRoomsView?.jumpToLogin()
                        }
                        Status.FAILED_UNKNOW -> {
                            getRoomsView?.jumpToLogin()
                            //getRoomsView?.showToast(R.string.error_unknow)
                        }
                    }
                }
            })
        }
    }
}