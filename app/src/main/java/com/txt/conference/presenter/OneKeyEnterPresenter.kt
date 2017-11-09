package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.ILoginView
import com.txt.conference.view.IOneKeyEnterView

/**
 * Created by pc on 2017/11/9.
 */
class OneKeyEnterPresenter {
    var mOneKeyView: IOneKeyEnterView? = null
    var mOneKeyEnterModel: IOneKeyEnterModel? = null

    init {

    }

    constructor(view: IOneKeyEnterView) {
        this.mOneKeyView = view
        mOneKeyEnterModel = OneKeyEnterModel()
       // initInfomation()
    }


    fun doOneKeyEnter(roomNo: String, userName: String) {
        mOneKeyView?.showLoading(0)
        mOneKeyEnterModel?.enter(roomNo, userName, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mOneKeyView?.hideLoading()
                when (mOneKeyEnterModel!!.status) {
                    Status.SUCCESS -> {
                        mOneKeyView?.hideError()
                        mOneKeyView?.jumpActivity((mOneKeyEnterModel as IOneKeyEnterModel).mOnKeyEnterBean.room!!)
                    }
                    Status.FAILED -> mOneKeyView?.showToast(mOneKeyEnterModel?.msg!!)
                    Status.FAILED_TOKEN_AUTH -> mOneKeyView?.showToast(mOneKeyEnterModel?.msg!!)
                    Status.FAILED_UNKNOW -> mOneKeyView?.showToast(R.string.error_unknow)
                }
            }

        })
    }
}