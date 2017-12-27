package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.ULog
import com.txt.conference.bean.CreateConferenceRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean
import com.txt.conference.http.DeleteRoomUserHttpFactory

/**
 * Created by pc on 2017/12/14.
 */
class DeleteRoomUserModel : IDeleteRoomUserModel {
    override var room: RoomBean? = null
    override var token: TokenBean? = null
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var deleteRoomHttp: DeleteRoomUserHttpFactory? = null

    override fun deleteRoomUser(room: RoomBean, uid: String, token: String, callBack: IBaseModel.IModelCallBackWithBean) {
        this.room = room
        if (deleteRoomHttp == null) {
            deleteRoomHttp = DeleteRoomUserHttpFactory()
            deleteRoomHttp?.setHttpEventHandler(object : HttpEventHandler<CreateConferenceRoomBean>() {
                override fun HttpSucessHandler(result: CreateConferenceRoomBean?) {
                    status = result?.code!!
                    ULog.i("tttt:", "code:" + result?.code!!)
                    if (status == Status.SUCCESS) {

                    } else {
                        msg = result?.msg
                    }
                    callBack.onStatus(result.data)
                }

                override fun HttpFailHandler() {
                    ULog.i("tttt:", " error")
                    status = Status.FAILED_UNKNOW
                    callBack.onStatus(null)
                }

            })
        }
        deleteRoomHttp?.DownloaDatas(room.roomId, uid, token)
    }
}