package com.txt.conference.presenter

import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.IFaceAuthView
import com.txt.conference.view.IFaceLoginView
import com.txt.conference.view.ILoginView

/**
 * Created by pc on 2017/12/1.
 */
class FaceAuthPresenter {
    var mFaceAuthView: IFaceAuthView? = null
    var mFaceAuthModel: IFaceAuthModel? = null

    init {

    }

    constructor(view: IFaceAuthView) {
        this.mFaceAuthView = view
        mFaceAuthModel = FaceAuthModel()
    }

    fun doAuthCheck(token: String, strPath: String) {
        mFaceAuthView?.showLoading(0)
        mFaceAuthModel?.authcheck(token, strPath, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mFaceAuthView?.hideLoading()
                when (mFaceAuthModel!!.status) {
                    Status.SUCCESS -> {
                        mFaceAuthView?.hideError()
                        mFaceAuthView?.jumpActivity()
                    }
                    Status.FAILED -> mFaceAuthView?.showToast(mFaceAuthModel?.msg!!)
                    Status.FAILED_TOKEN_AUTH -> mFaceAuthView?.showToast(mFaceAuthModel?.msg!!)
                    Status.FAILED_UNKNOW -> mFaceAuthView?.showToast(R.string.error_unknow)
                }
            }

        })
    }
}