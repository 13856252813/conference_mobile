package com.txt.conference.presenter

import com.txt.conference.bean.CreateRoomListAdapterBean
import com.txt.conference.model.CreateConferenceModel
import com.txt.conference.model.ICreateConferenceModel
import com.txt.conference.view.ICreateConferenceView


/**
 * Created by pc on 2017/10/16.
 */


class CreateConferencePresenter {
    var mCreateConferenceView: ICreateConferenceView
    var mCreateConferenceModel: ICreateConferenceModel

    init {

    }

    constructor(view: ICreateConferenceView) {
        this.mCreateConferenceView = view
        mCreateConferenceModel = CreateConferenceModel()
    }

    fun initListData(){
        var list = mCreateConferenceModel.getListData()
        mCreateConferenceView.initListViewData(list)
    }
}