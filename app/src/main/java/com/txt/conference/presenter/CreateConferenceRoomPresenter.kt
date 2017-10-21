package com.txt.conference.presenter

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

        mCreateRoomModel?.createroom(strJson, token, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                if (mCreateRoomModel?.status == Status.SUCCESS) {

                } else {

                }
            }

        })
        /*mLoginModel?.login(account, password, object : IBaseModel.IModelCallBack {
            override fun onStatus() {
                if (mLoginModel?.status == Status.SUCCESS) {
                    mLoginView?.hideError()
                    mLoginView?.jumpActivity((mLoginModel as ILoginModel).mLoginBean)
                } else {
                    mLoginView?.showError("")
                }
            }

        })*/
    }
}