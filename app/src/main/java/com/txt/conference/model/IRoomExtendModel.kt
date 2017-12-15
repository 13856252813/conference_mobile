package com.txt.conference.model

import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.bean.TokenBean


/**
 * Created by pc on 2017/12/12.
 */
interface IRoomExtendModel : IBaseModel {

    var token: TokenBean?
    var room: RoomBean?

    fun roomExtend(min: Int, room: RoomBean, token: String, callBack: IBaseModel.IModelCallBack)

}