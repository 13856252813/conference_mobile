package com.txt.conference.view

import com.txt.conference_common.WoogeenSurfaceRenderer

/**
 * Created by jane on 2017/10/16.
 */
interface IClientView {
    fun getConnectToken()

    fun connect(token: String)
    fun connectError()
    fun connectSuccess()

    fun startVideo()

    fun addRemoteView(remoteView: WoogeenSurfaceRenderer)
}