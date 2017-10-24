package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.GetAttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.http.GetAttendeeHttpFactory

/**
 * Created by jane on 2017/10/13.
 */
class GetUsersModel : IGetUsersModel {
    override var status: Int = Status.FAILED
    override var msg: String? = null
    override var users: List<AttendeeBean>? = null

    private var getUsersHttp: GetAttendeeHttpFactory? = null



    override fun loadUsers(token: String, callBack: IBaseModel.IModelCallBack) {
        if (getUsersHttp == null) {
            getUsersHttp = GetAttendeeHttpFactory()
            getUsersHttp?.setHttpEventHandler(object : HttpEventHandler<GetAttendeeBean>() {
                override fun HttpSucessHandler(result: GetAttendeeBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        users = result?.data
                    } else {
                        users = null
                        msg = result?.msg
                    }
                    callBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    callBack.onStatus()
                }
            })
        }
        getUsersHttp?.DownloaDatas(token)
    }

    override fun fullInviteUser(inviteUser: List<ParticipantBean>): List<AttendeeBean> {
        if (inviteUser == null || inviteUser.size == 0) {
            return users!!
        }
        var invite: ParticipantBean? = null
        var user: AttendeeBean? = null
        for (i in 0..inviteUser.size-1) {
            invite = inviteUser[i]
            for (j in 0..users!!.size-1) {
                user = users?.get(j)
                if (invite?.id.equals(user?.uid)) {
                    user?.invited = true
                }
            }
        }
        return users!!
    }
}