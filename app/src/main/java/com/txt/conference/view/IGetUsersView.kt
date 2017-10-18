package com.txt.conference.view

import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/10/11.
 */
interface IGetUsersView : IBaseView {
    fun getToken(): String?
    fun addAttendees(conference: List<AttendeeBean>?)
    fun jumpToLogin()
}