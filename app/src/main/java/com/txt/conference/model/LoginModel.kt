package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.UserBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.LoginHttpFactory

/**
 * Created by jane on 2017/10/9.
 */
class LoginModel : ILoginModel {
    override var statu: Int = ILoginModel.FAILED
    override lateinit var mLoginBean: LoginBean

    var mPreference: TxSharedPreferencesFactory? = null
    var mLoginHttp: LoginHttpFactory? = null

    fun saveUser(account: String?, password: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setAccount(account)
        mPreference?.setPassword(account)
    }

    fun saveToken(token: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setToken(token)
    }

    override fun login(account: String, password: String, loginCallBack: ILoginModel.ILoginCallBack) {
        if (mLoginHttp == null) {
            mLoginHttp = LoginHttpFactory()
            mLoginHttp?.setHttpEventHandler(object : HttpEventHandler<LoginBean>() {
                override fun HttpSucessHandler(result: LoginBean?) {
                    if (result?.code == 0){
                        mLoginBean = result
                        saveUser(account, password)
                        saveToken(result?.token)
                        statu = ILoginModel.SUCCESS
                    }
                    loginCallBack.onStatues()
                }

                override fun HttpFailHandler() {
                    statu = ILoginModel.FAILED
                    saveUser(null, null)
                    saveToken(null)
                    loginCallBack.onStatues()
                }
            })
        }
        mLoginHttp?.mUser = UserBean(account, password)
        mLoginHttp?.DownloaDatas()
    }
}