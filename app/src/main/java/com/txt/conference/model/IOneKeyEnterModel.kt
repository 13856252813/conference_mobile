package com.txt.conference.model

import com.txt.conference.bean.LoginBean


/**
 * Created by pc on 2017/11/8.
 */
interface IOneKeyEnterModel : IBaseModel {
    var mLoginBean: LoginBean

    fun getName(): String
    fun getRoomNo(): String
    fun enter(roomNo: String, userName: String, enterCallBack: IBaseModel.IModelCallBack)
}