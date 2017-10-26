package com.txt.conference.model

import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.ParticipantBean


/**
 * Created by jane on 2017/10/12.
 */
interface IGetUsersModel : IBaseModel {
    var users: ArrayList<AttendeeBean>?
    var inviteUser: List<ParticipantBean>?

    fun loadUsers(token: String, callBack: IBaseModel.IModelCallBack)
    fun fullInviteUser(): List<AttendeeBean>
    fun removeMySelf(uid: String)
}