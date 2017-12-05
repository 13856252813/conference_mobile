package com.txt.conference.view

import com.txt.conference.bean.LoginBean

/**
 * Created by pc on 2017/12/1.
 */
interface IFaceLoginView : IBaseView {
    fun jumpActivity(loginBean: LoginBean)
    fun getAccount(): String
    fun setAccount(account: String)

    fun showError(error: String)
    fun hideError()
}