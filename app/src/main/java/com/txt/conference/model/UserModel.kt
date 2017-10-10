package com.txt.conference.model

import com.txt.conference.bean.UserBean

/**
 * Created by jane on 2017/10/9.
 */
class UserModel: IUserModel {
    override fun setID(id: Int) {
    }

    override fun setFirstName(fristName: String) {
    }

    override fun setLastName(lastName: String) {
    }

    override fun load(id: Int): UserBean {
        return UserBean()
    }
}