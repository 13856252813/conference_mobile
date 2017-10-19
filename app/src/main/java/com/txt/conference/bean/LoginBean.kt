package com.txt.conference.bean

import java.io.Serializable

/**
 * Created by jane on 2017/10/12.
 */
class LoginBean : Serializable {
    /**
     * token : 0d4cca4c66d70fe3fa8858c5693f972929c5390a5ccbfd644cf889b066dbfe00
     * id : account\czj
     * username : 陈哲俊
     * createdRooms : [{"roomId":"rooms\\陈哲俊\\1506676560000\\1506676609608","roomNo":"453944","start":1506676560000,"duration":"5","creator":{"uid":"account\\czj","display":"陈哲俊"},"participants":["account\\abc"]}]
     * invitedRooms : [{"roomId":"rooms\\abc\\1506676800000\\1506676845191","roomNo":"888986","start":1506676800000,"duration":"5","creator":{"uid":"account\\abc","display":"abc"},"participants":["account\\czj"]}]
     * contacts : [{"display":"chenzhejun","mobile":"13651711567","email":"423624706@qq.com","presence":"offline","id":"account\\13651711567"}]
     * code : -1
     * msg : info is not found
     */

    var token: String? = null
    var id: String? = null
    var username: String? = null
    var code: Int = 0
    var msg: String? = null
}