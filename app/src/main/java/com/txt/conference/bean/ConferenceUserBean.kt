package com.txt.conference.bean

/**
 * Created by pc on 2017/10/18.
 */
open class ConferenceUserBean{
    var id: String = ""
    var name: String = ""
    var mobile: String = ""
    var email: String = ""
    var group: String = ""
    init {
    }

    constructor(){

    }

    constructor (uid: String, displayName: String, mobileNo: String, emailAddress: String,
                 groupType: String){
        this.id = uid
        this.name = displayName
        this.mobile = mobileNo
        this.email = emailAddress
        this.group = groupType
    }


}