package com.txt.conference.model

/**
 * Created by jane on 2017/10/11.
 */
interface IBaseModel {
    var status: Int

    interface IModelCallBack {
        fun onStatus()
    }
}