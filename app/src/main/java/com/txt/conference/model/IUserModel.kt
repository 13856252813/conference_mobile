package com.txt.conference.model

import com.txt.conference.bean.UserBean

/**
 * Created by jane on 2017/10/7.
 */
interface IUserModel {
    fun setID(id: Int)
    fun setFirstName(fristName: String)
    fun setLastName(lastName: String)

    fun load(id: Int): UserBean
}