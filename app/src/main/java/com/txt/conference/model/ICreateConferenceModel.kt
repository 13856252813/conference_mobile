package com.txt.conference.model

import com.txt.conference.bean.CreateRoomListAdapterBean

/**
 * Created by pc on 2017/10/16.
 */

interface ICreateConferenceModel {

    fun getListData(): ArrayList<CreateRoomListAdapterBean>

}