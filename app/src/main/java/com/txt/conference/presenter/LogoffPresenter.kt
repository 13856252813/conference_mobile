package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.ILogoffView

/**
 * Created by jane on 2017/10/7.
 */
class LogoffPresenter {
    var logoffView: ILogoffView? = null
    var logoffModel: ILogoffModel? = null

    init {

    }

    constructor(view: ILogoffView) {
        logoffView = view
        logoffModel = LogoffModel()
    }

    fun logoff(token: String?) {
        if (token == null) {
            logoffView?.jumpToLogin()
            return
        }
        logoffModel?.logoff(token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                if (logoffModel?.status == Status.SUCCESS) {
                    logoffView?.jumpToLogin()
                } else {
                    logoffView?.showError(R.string.error_re_login)
                    logoffView?.jumpToLogin()
                }
            }

        })
    }
}