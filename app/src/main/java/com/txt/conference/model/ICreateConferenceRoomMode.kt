package com.txt.conference.model

import com.txt.conference.bean.CreateConferenceRoomBean

/**
 * Created by jane on 2017/10/19.
 */
interface ICreateConferenceRoomMode : IBaseModel {

    fun createroom(strJson: String?, token: String?, loginCallBack: IBaseModel.IModelCallBack)
}