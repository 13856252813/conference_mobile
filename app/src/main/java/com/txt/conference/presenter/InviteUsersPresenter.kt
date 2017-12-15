package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.model.IBaseModel
import com.txt.conference.model.IInviteUsersModel
import com.txt.conference.model.InviteUsersModel
import com.txt.conference.model.Status
import com.txt.conference.view.IInviteUsersView

/**
 * Created by jane on 2017/10/26.
 */
class InviteUsersPresenter {
    var inviteUsersModel: IInviteUsersModel? = null
    var inviteUsersView: IInviteUsersView? = null

    constructor(view: IInviteUsersView) {
        inviteUsersView = view
        inviteUsersModel = InviteUsersModel()
    }

    fun changeInviteList(attendtype: Int, attendee: AttendeeBean) {
        inviteUsersModel?.changeInviteList(attendtype, attendee)
        inviteUsersView?.setAttendeeNumber(inviteUsersModel?.getInviteSize()!!)
    }

    fun invite(roomId: String?, token: String?) {
        if (token == null || token.equals("")){
            inviteUsersView?.jumpToLogin()
        } else {
            inviteUsersView?.showLoading(R.string.loading_add_attendee)
            inviteUsersModel?.invite(roomId!!, token, object : IBaseModel.IModelCallBack {
                override fun onStatus() {
                    inviteUsersView?.hideLoading()
                    when (inviteUsersModel!!.status) {
                        Status.SUCCESS -> inviteUsersView?.inviteComplete(inviteUsersModel?.room!!)
                        Status.FAILED -> inviteUsersView?.showToast(inviteUsersModel?.msg!!)
                        Status.FAILED_TOKEN_AUTH -> {
                            inviteUsersView?.showToast(R.string.error_re_login)
                            inviteUsersView?.jumpToLogin()
                        }
                        Status.FAILED_UNKNOW -> inviteUsersView?.showToast(R.string.error_unknow)
                    }
                }
            })
        }
    }
}