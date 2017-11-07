package com.txt.conference.model

import com.txt.conference.bean.CreateRoomListAdapterBean
import kotlinx.android.synthetic.main.activity_createconferenceroom.*
import com.txt.conference.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pc on 2017/10/16.
 */

class CreateConferenceModel : ICreateConferenceModel {


    override fun getListData(): ArrayList<CreateRoomListAdapterBean> {

        val formatter = SimpleDateFormat("MM-dd HH:mm")
        val curDate = Date(System.currentTimeMillis())//获取当前时间
        //var calendar: Calendar  = Calendar.getInstance()
        /*if (curDate.minutes< 58){
            calendar.set(curDate.year, curDate.month, curDate.day, curDate.hours, curDate.minutes + 2)
        } else {
            calendar.set(curDate.year, curDate.month, curDate.day, curDate.hours + 1, 0)
        }*/

        val str = formatter.format(curDate)
        val list = ArrayList<CreateRoomListAdapterBean>(4)
        var bean1: CreateRoomListAdapterBean = CreateRoomListAdapterBean()
        var bean2: CreateRoomListAdapterBean = CreateRoomListAdapterBean()
        var bean3: CreateRoomListAdapterBean = CreateRoomListAdapterBean()
        var bean4: CreateRoomListAdapterBean = CreateRoomListAdapterBean()
        bean1.icon = R.mipmap.mutiman
        bean1.strinfo = "添加参会人"
        bean1.strinfo2 = "0"
        bean1.icon2 = R.mipmap.right_arrow
        bean2.icon = R.mipmap.conference_device
        bean2.strinfo = "会议室设备"
        bean2.strinfo2 = "0"
        bean2.icon2 = R.mipmap.right_arrow
        bean3.icon = R.mipmap.starttime
        bean3.strinfo = "开始时间"
        bean3.strinfo2 = str
        bean3.icon2 = R.mipmap.right_arrow
        bean4.icon = R.mipmap.costtime
        bean4.strinfo = "持续时长"
        bean4.strinfo2 = "1"
        bean4.icon2 = R.mipmap.right_arrow
        list.add(bean1)
        list.add(bean2)
        list.add(bean3)
        list.add(bean4)
        return list
    }
}