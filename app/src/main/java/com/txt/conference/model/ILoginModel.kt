package com.txt.conference.model

import com.txt.conference.bean.LoginBean


/**
 * Created by jane on 2017/10/7.
 */
interface ILoginModel : IBaseModel {
    var mLoginBean: LoginBean

    fun getAccount(): String
    fun getPassword(): String
    fun login(account: String, password: String, loginCallBack: IBaseModel.IModelCallBack)
}