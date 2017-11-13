package com.txt.conference.model

import android.util.Log
import com.common.http.HttpEventHandler
import com.txt.conference.bean.CreateConferenceRoomBean
import com.txt.conference.http.CreateConferenceRoomHttpFactory

/**
 * Created by jane on 2017/10/9.
 */
class CreateConferenceRoomMode : ICreateConferenceRoomMode {
    override var msg: String? = null


    override var status: Int = Status.FAILED

    override var mCreateRoomHttp: CreateConferenceRoomHttpFactory? = null
    override var mCreateRoomBean: CreateConferenceRoomBean? = null

    override fun createroom(strJson: String?, token: String?, createCallBack: IBaseModel.IModelCallBack) {
        Log.i("mytest", "CreateConferenceRoomMode: create Room")
        if (mCreateRoomHttp == null) {
            Log.i("mytest", "CreateConferenceRoomMode: new CreateConferenceRoomHttpFactory")
            mCreateRoomHttp = CreateConferenceRoomHttpFactory()
            mCreateRoomHttp?.setHttpEventHandler(object : HttpEventHandler<CreateConferenceRoomBean>() {
                override fun HttpSucessHandler(result: CreateConferenceRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        mCreateRoomBean = result
                    } else {
                        mCreateRoomBean = null
                        msg = result?.msg
                    }
                    createCallBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    createCallBack.onStatus()
                }
            })
        }
        //mCreateRoomHttp?.mUser = UserBean(account, password)
        //Log.i("mytest", "CreateConferenceRoomMode: send post:" + strJson)
        mCreateRoomHttp?.mPostCreaetJsonStr = strJson
        mCreateRoomHttp?.DownloaDatas(token)
    }



}