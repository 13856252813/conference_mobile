package com.txt.conference.model

import com.txt.conference.bean.LoginBean


/**
 * Created by jane on 2017/10/7.
 */
interface ILoginModel : IBaseModel {
    var statu: Int
    var mLoginBean: LoginBean

    companion object {
        val SUCCESS = 1
        val FAILED = -1
    }

    fun login(account: String, password: String, loginCallBack: ILoginCallBack)

    interface ILoginCallBack {
        fun onStatues()
    }
}