package com.txt.conference.model

import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.ParticipantBean


/**
 * Created by jane on 2017/10/12.
 */
interface IGetUsersModel : IBaseModel {
    var users: List<AttendeeBean>?

    fun loadUsers(token: String, callBack: IBaseModel.IModelCallBack)
    fun fullInviteUser(inviteUser: List<ParticipantBean>): List<AttendeeBean>
}