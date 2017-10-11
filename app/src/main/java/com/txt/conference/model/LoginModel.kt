package com.txt.conference.model

/**
 * Created by jane on 2017/10/9.
 */
class LoginModel : ILoginModel {
    override fun login(account: String, password: String, loginCallBack: ILoginModel.ILoginCallBack) {
        loginCallBack.onStatues()
    }
}