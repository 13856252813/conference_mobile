package com.txt.conference.presenter

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
            getUsersModel?.loadUsers(token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    if (getUsersModel!!.status == Status.SUCCESS) {
                        getUsersView?.addAttendees(getUsersModel?.users)
                    }
                }
            })
        }
    }
}