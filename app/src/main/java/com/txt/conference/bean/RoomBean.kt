package com.txt.conference.bean

import java.io.Serializable

/**
 * Created by jane on 2017/10/12.
 */
class RoomBean : Serializable {
    /**
     * roomId : rooms\陈哲俊\1506676560000\1506676609608
     * roomNo : 453944
     * start : 1506676560000
     * duration : 5
     * creator : {"uid":"account\\czj","display":"陈哲俊"}
     * participants : ["account\\abc"]
     */
    companion object {
        val STATUS_NORMAL = 0
        val STATUS_BEGING = 1
        val STATUS_COUNT_DOWN = 3
    }

    var roomId: String? = null
    var roomNo: String? = null
    var start: Long = 0
    var duration: String? = null
    var creator: CreatorBean? = null
    var participants: List<String>? = null
    var status: Int = STATUS_NORMAL

    fun getDurationMillis(): Long {
        return  Integer.parseInt(duration) * 60 * 60 * 1000L
    }

}