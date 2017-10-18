package com.txt.conference.view

import android.content.Context
import com.txt.conference.bean.LoginBean

/**
 * Created by jane on 2017/10/7.
 */
interface ILogoffView : IBaseView {
    fun jumpToLogin()
    fun getToken(): String?
    fun showError(resId: Int)
}