package com.txt.conference.view

import com.txt.conference.bean.RoomBean


/**
 * Created by pc on 2017/11/17.
 */
interface IGetRoomInfoView : IBaseView {
    fun getToken(): String?
    fun getRoomInfoFinished(getRoom: Boolean?, roombean: RoomBean?)
    fun jumpToLogin()
}