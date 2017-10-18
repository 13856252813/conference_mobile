package com.txt.conference.view

import com.txt.conference.bean.LoginBean

/**
 * Created by jane on 2017/10/7.
 */
interface ILoginView : IBaseView {
    fun jumpActivity(loginBean: LoginBean)
    fun getAccount(): String
    fun getPassword(): String
    fun setAccount(account: String)
    fun setPassword(password: String)

    fun showError(error: String)
    fun hideError()
}