package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.InviteUsersBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.http.InviteUsersHttpFactory

/**
 * Created by jane on 2017/10/26.
 */
class InviteUsersModel : IInviteUsersModel {
    override var status: Int = Status.FAILED
    override var msg: String? = ""
    override var room: RoomBean? = null
    private var inviteUsersHttp: InviteUsersHttpFactory? = null
    var inviteMap: HashMap<String, AttendeeBean>? = null
    var attendType: Int = 0

    init {
        inviteMap = HashMap<String, AttendeeBean>()
    }

    override fun getInviteSize(): Int {
        if (inviteMap == null) return 0 else return inviteMap?.size!!
    }

    override fun changeInviteList(attendtype: Int, attendee: AttendeeBean) {
        attendType = attendtype
        if (attendee.invited) {
            inviteMap?.put(attendee.id!!, attendee)
        } else {
            inviteMap?.remove(attendee.id)
        }
    }

    override fun invite(roomId: String, token: String, callBack: IBaseModel.IModelCallBack) {
        if (inviteUsersHttp == null) {
            inviteUsersHttp = InviteUsersHttpFactory()
            inviteUsersHttp?.setHttpEventHandler(object : HttpEventHandler<InviteUsersBean>() {
                override fun HttpSucessHandler(result: InviteUsersBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        room = result?.data
                    } else {
                        room = null
                        msg = result?.msg
                    }
                    callBack.onStatus()
                }
                override fun HttpFailHandler() {
                    room = null
                    status = Status.FAILED_UNKNOW
                    callBack.onStatus()
                }
            })
        }
        inviteUsersHttp?.attendType = attendType
        inviteUsersHttp?.attendeeList = ArrayList(inviteMap?.values)
        inviteUsersHttp?.DownloaDatas(roomId, token)
    }
}