package com.txt.conference.model

import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/11.
 */
interface IBaseModel {
    var status: Int
    var msg: String?

    interface IModelCallBack {
        fun onStatus()
    }

    interface IModelCallBackWithBean {
        fun onStatus(bean:RoomBean?)
    }
}