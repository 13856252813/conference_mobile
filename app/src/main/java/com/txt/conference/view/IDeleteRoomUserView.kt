package com.txt.conference.view

import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/12/14.
 */
interface IDeleteRoomUserView : IBaseView {
    fun getToken(): String?
    fun jumpToLogin()
    fun deleteFinished()
    fun showError(errorRes: Int)
}