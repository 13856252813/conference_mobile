package com.txt.conference.model

import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/26.
 */
interface IInviteUsersModel: IBaseModel {
    var room: RoomBean?

    fun getInviteSize(): Int
    fun changeInviteList(attendee: AttendeeBean)
    fun invite(roomId: String, token: String, callBack: IBaseModel.IModelCallBack)
}