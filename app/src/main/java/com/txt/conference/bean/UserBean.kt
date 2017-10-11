package com.txt.conference.bean

/**
 * Created by jane on 2017/9/29.
 */
open class UserBean{
    var account: String = ""
    var password: String = ""

    init {
        println("hello init")
    }

    constructor(){

    }

    constructor (account: String, password: String){
        this.account = account
        this.password = password
    }


}