package com.txt.conference.presenter

import com.txt.conference.model.IUserModel
import com.txt.conference.model.UserModel
import com.txt.conference.view.IUserView

/**
 * Created by jane on 2017/10/7.
 */
class UserPresenter(userView: IUserView) {
    var mUserView: IUserView = userView
    var mUserModel: IUserModel = UserModel()

    init {

    }



}