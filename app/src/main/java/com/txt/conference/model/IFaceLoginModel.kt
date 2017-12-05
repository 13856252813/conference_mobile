package com.txt.conference.model

import com.txt.conference.bean.LoginBean


/**
 * Created by pc on 2017/12/1.
 */
interface IFaceLoginModel : IBaseModel {
    var mLoginBean: LoginBean

    fun getAccount(): String
    fun login(account: String, strpath: String, loginCallBack: IBaseModel.IModelCallBack)
}