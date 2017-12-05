package com.txt.conference.model

import com.common.bean.UpLoadInfo
import com.common.http.HttpEventHandler
import com.common.http.HttpUploadsFactoryBase
import com.common.utlis.ULog
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.FaceLoginHttpFactory
import com.txt.conference.http.LoginHttpFactory
import java.util.ArrayList


/**
 * Created by pc on 2017/12/1.
 */
class FaceLoginModel : IFaceLoginModel {
    override var status: Int = Status.FAILED
    override var msg: String? = null
    override lateinit var mLoginBean: LoginBean


    var nAccount = ""
    var mPreference: TxSharedPreferencesFactory? = null
    var mFaceLoginHttp: FaceLoginHttpFactory? = null

    fun saveUser(account: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setAccount(account)
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

    fun saveId(id: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setId(id)
    }

    fun saveIsLogin(type: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setLogin(type)
    }

    override fun getAccount(): String {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        return mPreference?.getAccount()!!
    }


    fun savePhoneNumber(phoneNumber: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setUserName(phoneNumber)
    }

    override fun login(account: String, strpath: String, loginCallBack: IBaseModel.IModelCallBack) {
        nAccount = account
        if (mFaceLoginHttp == null) {
            mFaceLoginHttp = FaceLoginHttpFactory()
            mFaceLoginHttp?.setHttpEventHandler(object : HttpEventHandler<GetLoginBean>() {
                override fun HttpSucessHandler(result: GetLoginBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        mLoginBean = result?.data!!
                        saveUser(nAccount)
                        saveToken(mLoginBean.token)
                        saveUserName(mLoginBean.username)
                        saveId(mLoginBean.id)
                        saveIsLogin("true")
                    } else {
                        mLoginBean = LoginBean()
                        saveIsLogin("false")
                        msg = result?.msg
                    }
                    loginCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    mLoginBean = LoginBean()
                    status = Status.FAILED_UNKNOW
                    saveToken(null)
                    saveId(null)
                    loginCallBack.onStatus()
                }
            })
        }

        var uploadlist = ArrayList<UpLoadInfo>()
        var uploadinfo = UpLoadInfo()            // String
        uploadinfo.keyname = "uaccount"
        uploadinfo.uploadPathOrString = nAccount
        uploadinfo.uploadtype = 0
        uploadlist.add(uploadinfo)
        var uploadinfoFile = UpLoadInfo()        // File
        uploadinfoFile.keyname = "faceimage"
        uploadinfoFile.uploadPathOrString = strpath
        uploadinfoFile.uploadtype = 1
        uploadinfoFile.mineType = "image/jpeg"
        uploadlist.add(uploadinfoFile)
        mFaceLoginHttp?.uploadInfos = uploadlist
        mFaceLoginHttp?.DownloaDatas()

    }
}