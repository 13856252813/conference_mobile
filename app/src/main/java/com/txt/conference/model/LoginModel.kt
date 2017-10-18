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
    override var status: Int = Status.FAILED
    override lateinit var mLoginBean: LoginBean

    var mPreference: TxSharedPreferencesFactory? = null
    var mLoginHttp: LoginHttpFactory? = null

    fun saveUser(account: String?, password: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setAccount(account)
        mPreference?.setPassword(password)
    }

    fun saveToken(token: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setToken(token)
    }

    fun saveUserName(userName: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setUserName(userName)
    }

    override fun getAccount(): String {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        return mPreference?.getAccount()!!
    }

    override fun getPassword():String {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        return mPreference?.getPassword()!!
    }

    fun savePhoneNumber(phoneNumber: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setUserName(phoneNumber)
    }

    override fun login(account: String, password: String, loginCallBack: IBaseModel.IModelCallBack) {
        if (mLoginHttp == null) {
            mLoginHttp = LoginHttpFactory()
            mLoginHttp?.setHttpEventHandler(object : HttpEventHandler<LoginBean>() {
                override fun HttpSucessHandler(result: LoginBean?) {
                    if (result?.code == 0){
                        mLoginBean = result
                        saveUser(account, password)
                        saveToken(result?.token)
                        saveUserName(result?.username)
//                        savePhoneNumber(result?.pho)
                        status = Status.SUCCESS
                    }
                    loginCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED
                    saveUser(null, null)
                    saveToken(null)
                    loginCallBack.onStatus()
                }
            })
        }
        mLoginHttp?.mUser = UserBean(account, password)
        mLoginHttp?.DownloaDatas()
    }
}