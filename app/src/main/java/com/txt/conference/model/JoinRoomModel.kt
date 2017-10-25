package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.JoinRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean
import com.txt.conference.http.JoinRoomHttpFactory

/**
 * Created by jane on 2017/10/15.
 */
class JoinRoomModel : IJoinRoomModel {
    override var room: RoomBean? = null
    override var token: TokenBean? = null
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var joinRoomHttp: JoinRoomHttpFactory? = null

    override fun joinRoom(room: RoomBean, token: String, callBack: IBaseModel.IModelCallBack) {
        this.room = room
        if (joinRoomHttp == null) {
            joinRoomHttp = JoinRoomHttpFactory()
            joinRoomHttp?.setHttpEventHandler(object : HttpEventHandler<JoinRoomBean>() {
                override fun HttpSucessHandler(result: JoinRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        this@JoinRoomModel.token = result.data
                    } else {
                        msg = result?.msg
                    }
                    callBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    callBack.onStatus()
                }

            })
        }
        joinRoomHttp?.DownloaDatas(room.roomId, token)
    }
}