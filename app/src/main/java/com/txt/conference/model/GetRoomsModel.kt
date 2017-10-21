package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.GetRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.http.GetRoomsHttpFactory

/**
 * Created by jane on 2017/10/13.
 */
class GetRoomsModel : IGetRoomsModel {
    override var status: Int = Status.FAILED
    override var msg: String? = null
    override var rooms: List<RoomBean>? = null

    private var getRoomsHttp: GetRoomsHttpFactory? = null



    override fun loadRooms(token: String, callBack: IBaseModel.IModelCallBack) {
        if (getRoomsHttp == null) {
            getRoomsHttp = GetRoomsHttpFactory()
            getRoomsHttp?.setHttpEventHandler(object : HttpEventHandler<GetRoomBean>() {
                override fun HttpSucessHandler(result: GetRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        rooms = result?.data
                    } else {
                        rooms = null
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
        getRoomsHttp?.DownloaDatas(token)
    }
}