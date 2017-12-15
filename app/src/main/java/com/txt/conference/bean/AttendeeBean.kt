package com.txt.conference.bean

import java.io.Serializable

/**
 * Created by jane on 2017/10/17.
 */
class AttendeeBean : Serializable {
    companion object {
        val ROLE_SELF = "self"
        val ROLE_CREATOR = "creator"
    }
    var id: String? = null
    var display: String? = null
    var mobile: String? = null
    var email: String? = null
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