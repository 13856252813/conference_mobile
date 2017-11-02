package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.DeleteRoomBean
import com.txt.conference.bean.JoinRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean
import com.txt.conference.http.DeleteRoomHttpFactory
import com.txt.conference.http.JoinRoomHttpFactory

/**
 * Created by jane on 2017/11/02.
 */
class DeleteRoomModel : IDeleteRoomModel {
    override var room: RoomBean? = null
    override var token: TokenBean? = null
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var deleteRoomHttp: DeleteRoomHttpFactory? = null

    override fun deleteRoom(room: RoomBean, token: String, callBack: IBaseModel.IModelCallBack) {
        this.room = room
        if (deleteRoomHttp == null) {
            deleteRoomHttp = DeleteRoomHttpFactory()
            deleteRoomHttp?.setHttpEventHandler(object : HttpEventHandler<DeleteRoomBean>() {
                override fun HttpSucessHandler(result: DeleteRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {

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
        deleteRoomHttp?.DownloaDatas(room.roomId, token)
    }
}