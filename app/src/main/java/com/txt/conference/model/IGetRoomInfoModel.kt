package com.txt.conference.model

import com.txt.conference.bean.RoomBean


/**
 * Created by jane on 2017/11/17.
 */
interface IGetRoomInfoModel : IBaseModel {
    var roominfo: RoomBean?

    fun loadRoomInfo(roomno: String, token: String, callBack: IBaseModel.IModelCallBack)
}