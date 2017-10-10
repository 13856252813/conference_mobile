package com.txt.conference.bean

/**
 * Created by jane on 2017/9/29.
 */
open class UserBean{
    var mFirstname: String = ""
    var mLastName: String = ""

    init {
        println("hello init")
    }

    constructor(){

    }

    constructor (firstname: String, lastName: String){
        println("constructor $firstname $lastName")
        this.mFirstname = firstname
        this.mLastName = lastName
    }


}