package com.txt.conference.view

/**
 * Created by jane on 2017/10/7.
 */
interface IUserView {
    fun getID(): Int
    fun getFristName(): String
    fun getLastName(): String

    fun setFirstName(fristName: String)
    fun setLastName(lastName: String)
}