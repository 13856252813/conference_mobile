package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.IGetUsersView

/**
 * Created by jane on 2017/10/12.
 */
class GetUsersPresenter {
    var getUsersView: IGetUsersView? = null
    var getUsersModel: IGetUsersModel? = null

    constructor(view: IGetUsersView) {
        getUsersView = view
        getUsersModel = GetUsersModel()
    }

    fun getUsers(token: String?) {
        if (token == null || token.equals("")){
            getUsersView?.jumpToLogin()
        } else {
            getUsersView?.showLoading(0)
            getUsersModel?.loadUsers(token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    getUsersView?.hideLoading()
                    when (getUsersModel!!.status) {
                        Status.SUCCESS -> getUsersView?.addAttendees(getUsersModel?.users)
                        Status.FAILED -> getUsersView?.showToast(getUsersModel?.msg!!)
                        Status.FAILED_TOKEN_AUTH -> {
                            getUsersView?.showToast(R.string.error_re_login)
                            getUsersView?.jumpToLogin()
                        }
                        Status.FAILED_UNKNOW -> getUsersView?.showToast(R.string.error_unknow)
                    }
                }
            })
        }
    }
}