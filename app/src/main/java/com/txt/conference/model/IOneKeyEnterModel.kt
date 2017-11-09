package com.txt.conference.model

import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.OneKeyEnterBean


/**
 * Created by pc on 2017/11/8.
 */
interface IOneKeyEnterModel : IBaseModel {
    var mOnKeyEnterBean: OneKeyEnterBean

    fun getName(): String
    fun getRoomNo(): String
    fun saveName(token: String?)
    fun saveRoomNo(token: String?)
    fun enter(roomNo: String, userName: String, enterCallBack: IBaseModel.IModelCallBack)
}