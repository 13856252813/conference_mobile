package com.txt.conference.model

import com.txt.conference.bean.LogoffBean


/**
 * Created by jane on 2017/10/7.
 */
interface ILogoffModel : IBaseModel {
    var loginoffBean: LogoffBean

    fun logoff(token: String, loginCallBack: IBaseModel.IModelCallBack)
}