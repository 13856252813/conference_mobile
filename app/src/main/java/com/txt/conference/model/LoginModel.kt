package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.LoginHttpFactory


/**
 * Created by jane on 2017/10/9.
 */
class LoginModel : ILoginModel {
    override var status: Int = Status.FAILED
    override var msg: String? = null
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
            mLoginHttp?.setHttpEventHandler(object : HttpEventHandler<GetLoginBean>() {
                override fun HttpSucessHandler(result: GetLoginBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        mLoginBean = result?.data!!
                        saveUser(account, password)
                        saveToken(mLoginBean.token)
                        saveUserName(mLoginBean.username)
//                        savePhoneNumber(result?.pho)
                    } else {
                        mLoginBean = LoginBean()
                        msg = result?.msg
                    }
                    loginCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    mLoginBean = LoginBean()
                    status = Status.FAILED_UNKNOW
                    saveUser(null, null)
                    saveToken(null)
                    loginCallBack.onStatus()
                }
            })
        }
        mLoginHttp?.account = account
        mLoginHttp?.password = password
        mLoginHttp?.DownloaDatas()
    }
}