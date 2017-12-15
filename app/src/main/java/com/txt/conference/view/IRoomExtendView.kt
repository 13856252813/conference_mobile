package com.txt.conference.view

import com.txt.conference.bean.RoomBean

/**
 * Created by pc on 2017/12/12.
 */
interface IRoomExtendView : IBaseView {
    fun getToken(): String?
    fun extendFinished(roomBean: RoomBean)
    fun extendFailed()
}