package com.txt.conference.model


/**
 * Created by jane on 2017/10/7.
 */
interface ILoginModel : IBaseModel {

    fun login(account: String, password: String, loginCallBack: ILoginCallBack)

    interface ILoginCallBack {
        fun onStatues()
    }
}