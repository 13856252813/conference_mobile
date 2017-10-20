package com.txt.conference.view

import com.txt.conference.bean.AttendeeBean
import com.txt.conference_common.WoogeenSurfaceRenderer

/**
 * Created by jane on 2017/10/16.
 */
interface IClientView {
    fun getConnectToken(): String

    fun addRemoteView(remoteView: WoogeenSurfaceRenderer)

    fun switchCamera(isFrontCamera: Boolean)

    fun setAlreadyAttendees(number: String)

    fun updateUsers(users: List<AttendeeBean>)
}