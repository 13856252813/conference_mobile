package com.txt.conference.bean

/**
 * Created by pc on 2017/11/10.
 */

open class AddTypeBean{
    var strinfo: String? = ""
    var icon: Int = 0

    init {
        println("init")
    }

    constructor(){

    }

    constructor (strinfo: String, icon: Int){
        this.icon = icon
        this.strinfo = strinfo

    }


}