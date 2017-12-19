package com.txt.conference.view

import com.txt.conference.bean.ParticipantBean
import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/17.
 */
interface IRoomView {
    fun setRoomNumber(number: String)
    fun setDurationTime(time: String)
    fun setAllAttendees(number: String)
    fun getInviteAttendees(): List<ParticipantBean>
    fun setInviteAbility(ability: Boolean)
    fun getCurrentUid(): String
    fun showExtendConfirm()
    fun updateRoomBean(room: RoomBean)
    fun end()
}