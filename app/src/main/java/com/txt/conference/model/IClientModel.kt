package com.txt.conference.model

import com.intel.webrtc.conference.User
import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/10/20.
 */
interface IClientModel : IBaseModel {
    var cameraIsOpen: Boolean
    var microphoneIsOpen: Boolean
    var loudIsOpen: Boolean
    fun getUsers(users: List<User>): List<AttendeeBean>
}