package com.txt.conference.model

import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean

/**
 * Created by jane on 2017/10/15.
 */
interface IJoinRoomModel : IBaseModel {
    var token: TokenBean?
    var room: RoomBean?

    fun joinRoom(room: RoomBean, token: String, callBack: IBaseModel.IModelCallBack)
}