package com.txt.conference.view

import com.txt.conference.bean.RoomBean


/**
 * Created by jane on 2017/10/7.
 */
interface ICreateConferenceRoomView : IBaseView {
    fun jumpActivity(roomBean: RoomBean)
    fun showError(error: String)
    fun hideError()
}