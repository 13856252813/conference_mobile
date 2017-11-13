package com.txt.conference.model

import com.txt.conference.bean.CreateRoomListAdapterBean
import kotlinx.android.synthetic.main.activity_createconferenceroom.*
import com.txt.conference.R
import com.txt.conference.bean.AddTypeBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pc on 2017/11/10.
 */

class GetAddTypeModel : IGetAddTypeModel {


    override fun getListData(): ArrayList<AddTypeBean> {

        val list = ArrayList<AddTypeBean>(3)
        var bean0: AddTypeBean = AddTypeBean()
        var bean1: AddTypeBean = AddTypeBean()
        var bean2: AddTypeBean = AddTypeBean()

        bean0.icon = R.mipmap.right_arrow
        bean0.strinfo = "企业通讯录"
        bean1.icon = R.mipmap.right_arrow
        bean1.strinfo = "会议室设备"
        bean2.icon = R.mipmap.right_arrow
        bean2.strinfo = "手机短信"
        list.add(bean0)
        list.add(bean1)
        list.add(bean2)
        return list
    }
}