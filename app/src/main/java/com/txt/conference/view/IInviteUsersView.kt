package com.txt.conference.view

import com.txt.conference.bean.RoomBean


/**
 * Created by jane on 2017/10/11.
 */
interface IInviteUsersView : IBaseView {
    fun setAttendeeNumber(number: Int)
    fun getToken(): String?
    fun inviteComplete(room: RoomBean)
    fun jumpToLogin()
    fun getRoomId(): String?
}