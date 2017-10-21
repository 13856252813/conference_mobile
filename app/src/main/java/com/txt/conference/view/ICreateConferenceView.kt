package com.txt.conference.view

import com.txt.conference.bean.CreateRoomListAdapterBean
/**
 * Created by pc on 2017/10/16.
 */

interface ICreateConferenceView {
    fun initListViewData(listdata: ArrayList<CreateRoomListAdapterBean>)

}