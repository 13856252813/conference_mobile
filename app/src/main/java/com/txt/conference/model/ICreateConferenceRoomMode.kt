package com.txt.conference.model

import com.txt.conference.bean.CreateConferenceRoomBean
import com.txt.conference.http.CreateConferenceRoomHttpFactory

/**
 * Created by jane on 2017/10/19.
 */
interface ICreateConferenceRoomMode : IBaseModel {

    var mCreateRoomHttp: CreateConferenceRoomHttpFactory?
    var mCreateRoomBean: CreateConferenceRoomBean?
    fun createroom(strJson: String?, token: String?, loginCallBack: IBaseModel.IModelCallBack)
}