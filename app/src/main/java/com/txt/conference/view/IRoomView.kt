package com.txt.conference.view

/**
 * Created by jane on 2017/10/17.
 */
interface IRoomView {
    fun setRoomNumber(number: String)
    fun setDurationTime(time: String)
    fun setAllAttendees(number: String)
    fun end()
}