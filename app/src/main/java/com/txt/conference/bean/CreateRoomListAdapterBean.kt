package com.txt.conference.bean

/**
 * Created by pc on 2017/10/15.
 */

open class CreateRoomListAdapterBean{
    var icon: Int = 0
    var strinfo: String? = ""
    var strinfo2: String? = ""
    var icon2: Int = 0

    init {
        println("init")
    }

    constructor(){

    }

    constructor (icon: Int, strinfo: String, strinfo2: String, icon2: Int){
        this.icon = icon
        this.strinfo = strinfo
        this.icon2 = icon2
        this.strinfo2 = strinfo2
    }


}