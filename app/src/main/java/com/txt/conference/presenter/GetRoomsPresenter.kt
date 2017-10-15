package com.txt.conference.presenter

import com.txt.conference.model.GetRoomsModel
import com.txt.conference.model.IGetRoomsModel
import com.txt.conference.model.Status
import com.txt.conference.view.IGetRoomsView

/**
 * Created by jane on 2017/10/12.
 */
class GetRoomsPresenter {
    var getRoomsView: IGetRoomsView? = null
    var getRoomsModel: IGetRoomsModel? = null

    constructor(view: IGetRoomsView) {
        getRoomsView = view
        getRoomsModel = GetRoomsModel()
    }

    fun getRooms(token: String?) {
        if (token == null || token.equals("")){
            getRoomsView?.jumpToLogin()
        } else {
            getRoomsModel?.loadRooms(token, object : IGetRoomsModel.IGetRoomCallBack {
                override fun onStatus() {
                    if (getRoomsModel!!.status == Status.SUCCESS) {
                        getRoomsView?.addConferences(getRoomsModel?.rooms)
                    }
                }
            })
        }
    }
}