package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.ULog
import com.txt.conference.bean.AddTypeBean
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.GetAttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.http.GetAttendeeHttpFactory

/**
 * Created by pc on 2017/11/10.
 */
interface IGetAddTypeModel {

    fun getListData(): ArrayList<AddTypeBean>
}