package com.txt.conference.presenter

import android.app.Activity
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
    var mContext: Activity? = null
    init {

    }

    constructor(context: Activity, view: IFaceAuthView) {
        this.mFaceAuthView = view
        this.mContext = context
        mFaceAuthModel = FaceAuthModel()
    }

    fun doAuthCheck(token: String, strPath: String) {
        mContext?.runOnUiThread { mFaceAuthView?.showLoading(0) }
        mFaceAuthModel?.authcheck(token, strPath, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mContext?.runOnUiThread {
                    mFaceAuthView?.hideLoading()
                    when (mFaceAuthModel!!.status) {
                        Status.SUCCESS -> {
                            mFaceAuthView?.hideError()
                            mFaceAuthView?.checkOK()
                        }
                        Status.FAILED -> {
                            mFaceAuthView?.showToast(mFaceAuthModel?.msg!!)
                            mFaceAuthView?.checkFailed()
                        }
                        Status.FAILED_TOKEN_AUTH -> {
                            mFaceAuthView?.showToast(mFaceAuthModel?.msg!!)
                            mFaceAuthView?.checkFailed()
                        }
                        Status.FAILED_UNKNOW -> {
                            mFaceAuthView?.showToast(R.string.error_face_collect_retry)
                            mFaceAuthView?.checkFailed()
                        }
                    }
                }
            }

        })
    }
}