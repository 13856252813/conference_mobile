package com.txt.conference.bean

/**
 * Created by pc on 2017/10/19.
 */
class CreateConferenceRoomBean {

    var code: Int = 0
    var data: CreateRoomBean? = null

    class CreateRoomBean {
        var roomId: String? = null
        var roomNo: Int = 0
        var creator: CreatorEntity? = null
        var participants: List<String>? = null
        var duration: Int? = null
        var createAt: Int? = null
        var start: Int? = null

    }

    class CreatorEntity {
        /**
         * uid : account\czj
         * display : 陈哲俊
         */

        var uid: String? = null
        var display: String? = null
    }

}