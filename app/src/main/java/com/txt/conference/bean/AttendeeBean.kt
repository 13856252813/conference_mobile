package com.txt.conference.bean

/**
 * Created by jane on 2017/10/17.
 */
class AttendeeBean {
    var uid: String? = null
    var display: String? = null
    get() {
        if (field != null && field?.contains(":")!!) {
            var arr = field?.split(":")
            if (arr?.size!! > 1) {
                field = arr[1]
            }
        }
        return field
    }

    var role: String? = null
    var invited: Boolean = false
    var cantchange: Boolean = false

}