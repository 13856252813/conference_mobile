package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.GetRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.RoomInfoBean
import com.txt.conference.http.GetRoomInfoHttpFactory
import com.txt.conference.http.GetRoomsHttpFactory

/**
 * Created by pc on 2017/11/17.
 */
class GetRoomInfoModel : IGetRoomInfoModel {

    override var status: Int = Status.FAILED
    override var msg: String? = null
    override var roominfo: RoomBean? = null

    private var getRoomInfoHttp: GetRoomInfoHttpFactory? = null

    override fun loadRoomInfo(roomno: String, token: String, callBack: IBaseModel.IModelCallBack) {
        if (getRoomInfoHttp == null) {
            getRoomInfoHttp = GetRoomInfoHttpFactory()
            getRoomInfoHttp?.setHttpEventHandler(object : HttpEventHandler<RoomInfoBean>() {
                override fun HttpSucessHandler(result: RoomInfoBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        roominfo = result?.data
                    } else {
                        roominfo = null
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
        getRoomInfoHttp?.DownloaDatas(roomno, token)
    }

}