package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.CreateConferenceRoomBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.UserBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.CreateConferenceRoomHttpFactory
import com.txt.conference.http.LoginHttpFactory

/**
 * Created by jane on 2017/10/9.
 */
class CreateConferenceRoomMode : ICreateConferenceRoomMode {

    override var status: Int = Status.FAILED

    var mCreateRoomHttp: CreateConferenceRoomHttpFactory? = null
    var mCreateRoomBean: CreateConferenceRoomBean? = null


    override fun createroom(token: String?, loginCallBack: IBaseModel.IModelCallBack) {
        if (mCreateRoomHttp == null) {
            mCreateRoomHttp = CreateConferenceRoomHttpFactory()
            mCreateRoomHttp?.setHttpEventHandler(object : HttpEventHandler<CreateConferenceRoomBean>() {
                override fun HttpSucessHandler(result: CreateConferenceRoomBean?) {
                    if (result?.code == 0){
                        mCreateRoomBean = result
                        status = Status.SUCCESS
                    }
                    loginCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED
                    loginCallBack.onStatus()
                }
            })
        }
        //mCreateRoomHttp?.mUser = UserBean(account, password)
        mCreateRoomHttp?.DownloaDatas(token)
    }



}