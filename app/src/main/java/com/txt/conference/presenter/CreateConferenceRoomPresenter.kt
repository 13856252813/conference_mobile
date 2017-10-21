package com.txt.conference.presenter

import android.util.Log
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
        Log.i("mytest", "strJson:" + strJson)
        mCreateRoomView?.showLoading(1)
        mCreateRoomModel?.createroom(strJson, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                mCreateRoomView?.hideLoading()
                if (mCreateRoomModel?.status == Status.SUCCESS) {
                    mCreateRoomView?.jumpActivity()

                } else {
                    mCreateRoomView?.showError("创建房间失败")
                }
            }

        })


    }
}