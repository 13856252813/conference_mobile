package com.txt.conference.view

import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean
import com.txt.conference_common.WoogeenSurfaceRenderer

/**
 * Created by jane on 2017/10/16.
 */
interface IClientView : IBaseView{
    fun getConnectToken(): String

    fun addRemoteView(remoteView: WoogeenSurfaceRenderer)

    fun switchCamera(isFrontCamera: Boolean)

    fun onOffCamera(isOpenCamera: Boolean)

    fun isMicrophoneMute(isMicrophoneMute: Boolean)

    fun setAlreadyAttendees(number: String)

    fun updateUsers(users: List<AttendeeBean>)

    fun onOffLoud(isOpenLoud: Boolean)

    fun onJoined()

    fun updateRoomBean(room: RoomBean)

    fun switchAudioMode(isAudio:Boolean)

    fun setScreenIconInvisible()
}