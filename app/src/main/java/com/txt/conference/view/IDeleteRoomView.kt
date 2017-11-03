package com.txt.conference.view

import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/15.
 */
interface IDeleteRoomView : IBaseView {
    fun getToken(): String?
    fun jumpToLogin()
    fun deleteFinished()
    fun showError(errorRes: Int)
}