package com.txt.conference.model

import java.io.Serializable

/**
 * Created by pc on 2017/12/26.
 */

class MuteMediaBean : Serializable {


    /**
     * muteDes : 1
     * muteType : audioMute
     * room : {"creator":{"uid":"account/abc","display":"abc"},"roomNo":"685285","topic":"无会议主题","start":1514276580000,"participants":[{"audioMute":1,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"}],"delaytime":0,"duration":1,"roomId":"rooms/abc/1514276580000/1514276616009","id":"5a420710396fbb091c9c8348","createAt":1514276616009}
     * eventCode : muteUser
     */

    var muteDes: Int = 0
    var muteType: String? = null
    var room: RoomBean? = null
    var eventCode: String? = null

    class RoomBean {
        /**
         * creator : {"uid":"account/abc","display":"abc"}
         * roomNo : 685285
         * topic : 无会议主题
         * start : 1514276580000
         * participants : [{"audioMute":1,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"}]
         * delaytime : 0
         * duration : 1
         * roomId : rooms/abc/1514276580000/1514276616009
         * id : 5a420710396fbb091c9c8348
         * createAt : 1514276616009
         */

        var creator: CreatorBean? = null
        var roomNo: String? = null
        var topic: String? = null
        var start: Long = 0
        var delaytime: Int = 0
        var duration: Int = 0
        var roomId: String? = null
        var id: String? = null
        var createAt: Long = 0
        var participants: List<ParticipantsBean>? = null

        class CreatorBean {
            /**
             * uid : account/abc
             * display : abc
             */

            var uid: String? = null
            var display: String? = null
        }

        class ParticipantsBean {
            /**
             * audioMute : 1
             * name : 方林
             * videoMute : 0
             * mobile : 13856252813
             * id : account/fl
             * usertype : account
             * email : lin.fang@pivosgroup.com.cn
             */

            var audioMute: Int = 0
            var name: String? = null
            var videoMute: Int = 0
            var mobile: String? = null
            var id: String? = null
            var usertype: String? = null
            var email: String? = null
        }
    }
}
