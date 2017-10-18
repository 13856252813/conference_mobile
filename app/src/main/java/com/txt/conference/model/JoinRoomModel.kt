package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.JoinRoomBean
import com.txt.conference.bean.TokenBean
import com.txt.conference.http.JoinRoomHttpFactory

/**
 * Created by jane on 2017/10/15.
 */
class JoinRoomModel : IJoinRoomModel {
    override var token: TokenBean? = null
    override var status: Int = Status.FAILED

    var joinRoomHttp: JoinRoomHttpFactory? = null

    override fun joinRoom(roomId: String, token: String, callBack: IBaseModel.IModelCallBack) {
        if (joinRoomHttp == null) {
            joinRoomHttp = JoinRoomHttpFactory()
            joinRoomHttp?.setHttpEventHandler(object : HttpEventHandler<JoinRoomBean>() {
                override fun HttpSucessHandler(result: JoinRoomBean?) {
                    if (result?.code == 0 && result?.data != null && result?.data?.token != null) {
                        status = Status.SUCCESS
                        this@JoinRoomModel.token = result.data
                    } else {
                        status = Status.FAILED
                    }
                    callBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED
                    callBack.onStatus()
                }

            })
        }
        joinRoomHttp?.DownloaDatas(roomId, token)
    }
}