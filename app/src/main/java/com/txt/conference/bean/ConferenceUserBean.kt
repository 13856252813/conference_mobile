package com.txt.conference.bean

/**
 * Created by pc on 2017/10/18.
 */
open class ConferenceUserBean{
    var uid: String = ""
    var display: String = ""

    init {
    }

    constructor(){

    }

    constructor (uid: String, displayName: String){
        this.uid = uid
        this.display = displayName
    }


}