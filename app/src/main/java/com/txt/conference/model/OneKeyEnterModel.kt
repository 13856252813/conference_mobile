package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.ULog
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.bean.GetOneKeyEnterBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.OneKeyEnterBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.LoginHttpFactory
import com.txt.conference.http.OneKeyEnterHttpFactory


/**
 * Created by jane on 2017/11/9.
 */
class OneKeyEnterModel : IOneKeyEnterModel {



    override lateinit var mOnKeyEnterBean: OneKeyEnterBean
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var nroomNo = ""
    var nuserName = ""
    var mPreference: TxSharedPreferencesFactory? = null
    var mOneKeyEnterHttp: OneKeyEnterHttpFactory? = null

    override fun getName(): String {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        return mPreference?.getOneKeyName()!!
    }

    override fun getRoomNo(): String {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        return mPreference?.getOneKeyEnterRoomNo()!!
    }

    override fun saveName(name: String?){
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setOneKeyName(name)
    }

    override fun saveRoomNo(roomno: String?){
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setOneKeyEnterRoomNo(roomno)
    }

    fun saveToken(token: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setToken(token)
    }

    override fun enter(roomNo: String, userName: String, oneKeyEnterCallBack: IBaseModel.IModelCallBack) {
        nroomNo = roomNo
        nuserName = userName
        if (mOneKeyEnterHttp == null) {
            mOneKeyEnterHttp = OneKeyEnterHttpFactory()
            mOneKeyEnterHttp?.setHttpEventHandler(object : HttpEventHandler<GetOneKeyEnterBean>() {
                override fun HttpSucessHandler(result: GetOneKeyEnterBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        mOnKeyEnterBean = result?.data!!
                        saveToken(mOnKeyEnterBean.token)
                        saveName(mOnKeyEnterBean.username)
                        saveRoomNo(mOnKeyEnterBean.room?.roomNo)

//                        savePhoneNumber(result?.pho)
                    } else {
                        mOnKeyEnterBean = OneKeyEnterBean()
                        msg = result?.msg
                    }
                    oneKeyEnterCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    mOnKeyEnterBean = OneKeyEnterBean()
                    status = Status.FAILED_UNKNOW
                    saveToken(null)
                    saveName(null)
                    saveRoomNo(null)
                    oneKeyEnterCallBack.onStatus()
                }
            })
        }
        mOneKeyEnterHttp?.roomNo = roomNo
        mOneKeyEnterHttp?.userName = userName
        mOneKeyEnterHttp?.DownloaDatas()
    }
}