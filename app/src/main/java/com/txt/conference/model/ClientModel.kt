package com.txt.conference.model

import com.intel.webrtc.conference.User
import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/10/20.
 */
class ClientModel : IClientModel {
    override var msg: String? = null
    override var microphoneIsOpen: Boolean = true
    override var cameraIsOpen: Boolean = true
    override var loudIsOpen: Boolean = true
    override var status: Int = Status.FAILED

    override fun getUsers(users: List<User>): List<AttendeeBean> {
        var nUsers = ArrayList<AttendeeBean>()
        var user: User
        var attendee: AttendeeBean

        for (i in 0..users.size - 1) {
            user = users.get(i)
            attendee = AttendeeBean()
            attendee.display = user.name
            attendee.role = user.role

            nUsers.add(attendee)
        }
        return nUsers as List<AttendeeBean>
    }
}