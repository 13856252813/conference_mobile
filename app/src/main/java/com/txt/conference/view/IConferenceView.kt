package com.txt.conference.view

import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/11.
 */
interface IConferenceView : IBaseView {
    fun getToken(): String?
    fun addConferences(conference: List<RoomBean>?)
    fun jumpToLogin()
}