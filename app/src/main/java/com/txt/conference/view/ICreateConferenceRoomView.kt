package com.txt.conference.view

import com.txt.conference.bean.LoginBean

/**
 * Created by jane on 2017/10/7.
 */
interface ICreateConferenceRoomView : IBaseView {
    fun jumpActivity(loginBean: LoginBean)
    fun showError(error: String)
    fun hideError()
}