package com.txt.conference.view

import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.RoomBean

/**
 * Created by pc on 2017/11/9.
 */
interface IOneKeyEnterView : IBaseView {
    fun jumpActivity(roomBean: RoomBean)
    fun getRoomNo(): String
    fun getUserName(): String
    fun setRoomNo(roomno: String)
    fun setUserName(username: String)

    fun showError(error: String)
    fun hideError()
}