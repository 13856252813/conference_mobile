package com.txt.conference.presenter

import android.util.Log
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.model.*
import com.txt.conference.view.ICreateConferenceRoomView
import com.txt.conference.view.ILoginView

/**
 * Created by pc on 2017/10/19.
 */
class CreateConferenceRoomPresenter {
    var mCreateRoomView: ICreateConferenceRoomView? = null
    var mCreateRoomModel: ICreateConferenceRoomMode? = null

    init {

    }

    constructor(view: ICreateConferenceRoomView) {
        this.mCreateRoomView = view
        mCreateRoomModel = CreateConferenceRoomMode()
        initInfomation()
    }

    private fun initInfomation() {

    }

    fun doCreate(strJson: String?, token: String?) {
        //ULog.i("doCreate", "strJson:" + strJson)
        mCreateRoomView?.showLoading(R.string.createing_room)
        mCreateRoomModel?.createroom(strJson, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mCreateRoomView?.hideLoading()
                if (mCreateRoomModel?.status == Status.SUCCESS) {
                    //mCreateRoomView?.jumpActivity()
                    mCreateRoomView?.jumpActivity(mCreateRoomModel?.mCreateRoomBean?.data!!)
                } else if (mCreateRoomModel?.status == Status.FAILED_TOKEN_AUTH ) {
                    mCreateRoomView?.showToast(R.string.error_re_login)
                    mCreateRoomView?.jumpToLogin()
                }else {
                    mCreateRoomView?.showToast(R.string.createing_room_failed)
                }
            }
        })
    }
}