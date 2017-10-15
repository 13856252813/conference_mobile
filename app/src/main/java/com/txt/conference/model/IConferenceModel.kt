package com.txt.conference.model

import com.txt.conference.bean.RoomBean


/**
 * Created by jane on 2017/10/12.
 */
interface IConferenceModel : IBaseModel {
    var status: Int
    var rooms: List<RoomBean>?

    fun loadRooms(token: String, callBack: IGetRoomCallBack)

    interface IGetRoomCallBack {
        fun onStatus()
    }
}