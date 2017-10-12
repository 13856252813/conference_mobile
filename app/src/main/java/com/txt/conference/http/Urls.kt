package com.txt.conference.http

/**
 * Created by jane on 2017/10/12.
 */
object Urls {
    private var HOST = "http://192.168.16.65:3301"

    var LOGIN = HOST + "/login"
    var GET_ROOMS = HOST + "/api/getRooms/?token=%s"
}