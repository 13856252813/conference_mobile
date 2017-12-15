package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.ULog
import com.txt.conference.bean.CreateConferenceRoomBean
import com.txt.conference.bean.JoinRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean
import com.txt.conference.http.JoinRoomHttpFactory
import com.txt.conference.http.RoomExtendHttpFactory

/**
 * Created by pc on 2017/12/12.
 */
class RoomExtendModel : IRoomExtendModel {

    override var room: RoomBean? = null
    override var token: TokenBean? = null
    override var status: Int = Status.FAILED
    override var msg: String? = null
    var roomExtendHttp: RoomExtendHttpFactory? = null
    var mCreateRoomBean: CreateConferenceRoomBean? = null

    override fun roomExtend(min: Int, room: RoomBean, token: String, callBack: IBaseModel.IModelCallBack) {
        if (roomExtendHttp == null) {
            roomExtendHttp = RoomExtendHttpFactory()
            roomExtendHttp?.setHttpEventHandler(object : HttpEventHandler<CreateConferenceRoomBean>() {
                override fun HttpSucessHandler(result: CreateConferenceRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        mCreateRoomBean = result
                    } else {
                        mCreateRoomBean = null
                        msg = result?.msg
                    }
                    callBack.onStatus()
                    //ULog.i("ttmptest", "HttpSucessHandler")
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    //ULog.i("ttmptest", "HttpFailHandler")
                    callBack.onStatus()
                }
            })
        }
        roomExtendHttp?.DownloaDatas(room.roomId, token, min)
    }
}