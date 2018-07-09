package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.LogoffBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.LogoffHttpFactory

/**
 * Created by jane on 2017/10/9.
 */
class LogoffModel : ILogoffModel {
    override lateinit var loginoffBean: LogoffBean
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var mPreference: TxSharedPreferencesFactory? = null
    var mLogoffHttp: LogoffHttpFactory? = null

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
    fun saveLogin(login: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setLogin(login)
    }

    fun saveUserName(userName: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setUserName(userName)
    }

    fun savePhoneNumber(phoneNumber: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setUserName(phoneNumber)
    }

    override fun logoff(token: String, loginCallBack: IBaseModel.IModelCallBack) {
        if (mLogoffHttp == null) {
            mLogoffHttp = LogoffHttpFactory()
            mLogoffHttp?.setHttpEventHandler(object : HttpEventHandler<LogoffBean>() {
                override fun HttpSucessHandler(result: LogoffBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
//                        saveUser(null, null)
                        saveLogin("false")
                        saveToken(null)
                        saveUserName(null)
                        savePhoneNumber(null)
                    } else {
                        msg = result?.msg
                    }
                    loginCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
//                    saveUser(null, null)
                    saveToken(null)
                    saveUserName(null)
                    savePhoneNumber(null)
                    loginCallBack.onStatus()
                }
            })
        }
        mLogoffHttp?.token = token
        mLogoffHttp?.DownloaDatas()
    }
}