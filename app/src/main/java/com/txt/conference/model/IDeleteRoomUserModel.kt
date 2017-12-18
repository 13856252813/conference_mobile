package com.txt.conference.model

import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean

/**
 * Created by pc on 2017/12/13.
 */
interface IDeleteRoomUserModel : IBaseModel {
    var token: TokenBean?
    var room: RoomBean?

    fun deleteRoomUser(room: RoomBean, uid: String, token: String, callBack: IBaseModel.IModelCallBack)
}