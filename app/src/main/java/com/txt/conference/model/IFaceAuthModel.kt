package com.txt.conference.model

import com.txt.conference.bean.LoginBean


/**
 * Created by pc on 2017/12/1.
 */
interface IFaceAuthModel : IBaseModel {

    fun authcheck(token: String, strpath: String, loginCallBack: IBaseModel.IModelCallBack)
}