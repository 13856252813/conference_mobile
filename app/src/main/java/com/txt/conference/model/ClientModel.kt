package com.txt.conference.model

import com.common.utlis.ULog
import com.intel.webrtc.conference.User
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/20.
 */
class ClientModel : IClientModel {
    override var msg: String? = null
    override var microphoneIsOpen: Boolean = true
    override var cameraIsOpen: Boolean = true
    override var loudIsOpen: Boolean = true
    override var status: Int = Status.FAILED

    override fun getUsers(users: List<User>, room: RoomBean): List<AttendeeBean> {
        var nUsers = ArrayList<AttendeeBean>()
        var user: User
        var attendee: AttendeeBean
        var muteVideo = 0
        var muteAudio = 0
        var display: String
        for (i in 0..users.size - 1) {
            user = users.get(i)
            display = user.name

            if (room != null ) {
                if (display.endsWith(room.creator!!.uid!!)){
                    display = room.creator!!.display!!
                } else {

                    if (room.participants?.size!! > 0) {
                        for (j in 0..room.participants?.size!! - 1) {
                            if (display.endsWith(room.participants?.get(j)!!.id!!)) {
                                display = room.participants?.get(j)!!.name!!
                                muteVideo = room.participants?.get(j)!!.videoMute
                                muteAudio = room.participants?.get(j)!!.audioMute
                            }
                        }
                    }
                }
            }

            attendee = AttendeeBean()
            attendee.display = display
            attendee.id = user.name
            attendee.role = user.role
            attendee.audioMute = muteAudio.toString()
            attendee.videoMute = muteVideo.toString()
            nUsers.add(attendee)
        }
        return nUsers as List<AttendeeBean>
    }
}