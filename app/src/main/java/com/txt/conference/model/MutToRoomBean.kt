package com.txt.conference.model

import java.io.Serializable

/**
 * Created by pc on 2017/12/14.
 */

class MutToRoomBean : Serializable {


    /**
     * action : compere
     * uid : account/abc
     * eventCode : muteToUsers
     * room : {"creator":{"uid":"account/czj","display":"陈哲俊"},"roomNo":"626358","topic":"无会议主题","start":1513248840000,"participants":[{"audioMute":0,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"},{"audioMute":0,"name":"abc","videoMute":1,"mobile":"abc","id":"account/abc","usertype":"account","email":"abc@pivosgroup.com.cn"}],"delaytime":0,"duration":1,"roomId":"rooms/陈哲俊/1513248840000/1513248823079","id":"5a32586bd2c35b3079a08205","createAt":1513248823079}
     */

    var action: String? = null
    var uid: String? = null
    var eventCode: String? = null
    var room: RoomBean? = null

    class RoomBean : Serializable {
        /**
         * creator : {"uid":"account/czj","display":"陈哲俊"}
         * roomNo : 626358
         * topic : 无会议主题
         * start : 1513248840000
         * participants : [{"audioMute":0,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"},{"audioMute":0,"name":"abc","videoMute":1,"mobile":"abc","id":"account/abc","usertype":"account","email":"abc@pivosgroup.com.cn"}]
         * delaytime : 0
         * duration : 1
         * roomId : rooms/陈哲俊/1513248840000/1513248823079
         * id : 5a32586bd2c35b3079a08205
         * createAt : 1513248823079
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

        class CreatorBean : Serializable {
            /**
             * uid : account/czj
             * display : 陈哲俊
             */

            var uid: String? = null
            var display: String? = null
        }

        class ParticipantsBean : Serializable {
            /**
             * audioMute : 0
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
