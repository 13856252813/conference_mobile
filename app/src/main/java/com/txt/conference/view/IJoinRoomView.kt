package com.txt.conference.view

import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/15.
 */
interface IJoinRoomView : IBaseView {
    fun getToken(): String?
    fun jumpToLogin()
    fun jumpToRoom(room: RoomBean, connect_token: String)
    fun showError(errorRes: Int)
}