package com.txt.conference.presenter

import android.app.Activity
import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.IFaceLoginView
import com.txt.conference.view.ILoginView

/**
 * Created by pc on 2017/12/1.
 */
class FaceLoginPresenter {
    var mFaceLoginView: IFaceLoginView? = null
    var mFaceLoginModel: IFaceLoginModel? = null
    var mContext: Activity? = null
    init {

    }

    constructor(context: Activity, view: IFaceLoginView) {
        this.mFaceLoginView = view
        this.mContext = context
        mFaceLoginModel = FaceLoginModel()
        initInfomation()
    }

    private fun initInfomation() {
        mFaceLoginView?.setAccount(mFaceLoginView?.getAccount()!!)
    }

    fun doFaceLogin(account: String, strPath: String) {

        mContext?.runOnUiThread {
            mFaceLoginView?.showLoading(R.string.account_face_confirming)
        }
        mFaceLoginModel?.login(account, strPath, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mContext?.runOnUiThread {
                    mFaceLoginView?.hideLoading()
                }
                if (!(mContext!!.isFinishing)) {
                    when (mFaceLoginModel!!.status) {
                        Status.SUCCESS -> {
                            mContext?.runOnUiThread {
                                mFaceLoginView?.hideError()
                                //if (!(mContext!!.isFinishing)) {
                                mFaceLoginView?.jumpActivity((mFaceLoginModel as IFaceLoginModel).mLoginBean)
                                //}
                            }
                        }
                        Status.FAILED -> mContext?.runOnUiThread { mFaceLoginView?.showToast(mFaceLoginModel?.msg!!) }
                        Status.FAILED_TOKEN_AUTH -> mContext?.runOnUiThread { mFaceLoginView?.showToast(mFaceLoginModel?.msg!!) }
                        Status.FAILED_UNKNOW -> mContext?.runOnUiThread { mFaceLoginView?.showToast(R.string.error_facelogin_error_retry) }
                    }
                }
            }

        })
    }
}