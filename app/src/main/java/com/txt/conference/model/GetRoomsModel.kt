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
    override var rooms: List<RoomBean>? = null

    private var getRoomsHttp: GetRoomsHttpFactory? = null



    override fun loadRooms(token: String, callBack: IBaseModel.IModelCallBack) {
        if (getRoomsHttp == null) {
            getRoomsHttp = GetRoomsHttpFactory()
            getRoomsHttp?.setHttpEventHandler(object : HttpEventHandler<GetRoomBean>() {
                override fun HttpSucessHandler(result: GetRoomBean?) {
                    if (result?.code == 0) {
                        status = Status.SUCCESS
                        rooms = result?.rooms
                    } else {
                        rooms = null
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
        getRoomsHttp?.DownloaDatas(token)
    }
}