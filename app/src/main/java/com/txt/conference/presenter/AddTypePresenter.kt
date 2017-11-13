package com.txt.conference.presenter

import com.txt.conference.model.GetAddTypeModel
import com.txt.conference.model.IGetAddTypeModel
import com.txt.conference.view.IGetAddTypeView


/**
 * Created by pc on 2017/11/10.
 */


class AddTypePresenter {
    var mGetAddTypeView: IGetAddTypeView
    var mGetAddTypeModel: IGetAddTypeModel

    init {

    }

    constructor(view: IGetAddTypeView) {
        this.mGetAddTypeView = view
        mGetAddTypeModel = GetAddTypeModel()
    }

    fun initAddTypeViewData(){
        var list = mGetAddTypeModel.getListData()
        mGetAddTypeView.initAddTypeViewData(list)
    }
}