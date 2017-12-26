package com.txt.conference.presenter

import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.model.GetUsersModel
import com.txt.conference.model.IBaseModel
import com.txt.conference.model.IGetUsersModel
import com.txt.conference.model.Status
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

    fun getInvitedUserSize(): Int {
        return if (getUsersModel?.inviteUser == null) 0 else getUsersModel?.inviteUser?.size!!
    }

    fun getUsers(token: String?, alreadyInvite: List<ParticipantBean>) {
        ULog.d("GetUsersPresenter","---alreadyInviteSize:"+alreadyInvite.size)
        if (token == null || token.equals("")){
            getUsersView?.jumpToLogin()
        } else {
            getUsersView?.showLoading(0)
            getUsersModel?.inviteUser = alreadyInvite
            getUsersModel?.loadUsers(token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    getUsersView?.hideLoading()
                    when (getUsersModel!!.status) {
                        Status.SUCCESS -> {
                            getUsersModel?.fullInviteUser()
                            getUsersView?.setAttendeeNumber(0)

                            getUsersModel?.removeMySelf(getUsersView?.getUid()!!)
                            getUsersView?.setAttendeeAllNumber(getUsersModel?.users?.size!!)
                            getUsersView?.addAttendees(getUsersModel?.users)
                        }
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