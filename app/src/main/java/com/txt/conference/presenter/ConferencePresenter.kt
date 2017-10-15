package com.txt.conference.presenter

import com.txt.conference.model.ConferenceModel
import com.txt.conference.model.IConferenceModel
import com.txt.conference.model.Status
import com.txt.conference.view.IConferenceView

/**
 * Created by jane on 2017/10/12.
 */
class ConferencePresenter {
    var mConfrenceView: IConferenceView? = null
    var mConfrenceModel: IConferenceModel? = null

    constructor(view: IConferenceView) {
        mConfrenceView = view
        mConfrenceModel = ConferenceModel()
    }

    fun getRooms(token: String?) {
        if (token == null || token.equals("")){
            mConfrenceView?.jumpToLogin()
        } else {
            mConfrenceModel?.loadRooms(token, object : IConferenceModel.IGetRoomCallBack {
                override fun onStatus() {
                    if (mConfrenceModel!!.status == Status.SUCCESS) {
                        mConfrenceView?.addConferences(mConfrenceModel?.rooms)
                    }
                }
            })
        }
    }
}