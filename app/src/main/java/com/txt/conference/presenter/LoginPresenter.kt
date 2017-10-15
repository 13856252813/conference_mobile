package com.txt.conference.presenter

import com.txt.conference.model.IBaseModel
import com.txt.conference.model.ILoginModel
import com.txt.conference.model.LoginModel
import com.txt.conference.model.Status
import com.txt.conference.view.ILoginView

/**
 * Created by jane on 2017/10/7.
 */
class LoginPresenter {
    var mLoginView: ILoginView? = null
    var mLoginModel: ILoginModel? = null

    init {

    }

    constructor(view: ILoginView) {
        this.mLoginView = view
        mLoginModel = LoginModel()
    }

    fun doLogin(account: String, password: String) {
        mLoginModel?.login(account, password, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                if (mLoginModel?.status == Status.SUCCESS) {
                    mLoginView?.hideError()
                    mLoginView?.jumpActivity((mLoginModel as ILoginModel).mLoginBean)
                } else {
                    mLoginView?.showError("")
                }
            }

        })
    }
}