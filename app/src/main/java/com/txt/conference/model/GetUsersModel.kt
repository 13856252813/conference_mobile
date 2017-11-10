package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.ULog
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.GetAttendeeBean
import com.txt.conference.bean.ParticipantBean
import com.txt.conference.http.GetAttendeeHttpFactory

/**
 * Created by jane on 2017/10/13.
 */
class GetUsersModel : IGetUsersModel {
    override var inviteUser: List<ParticipantBean>? = null
    override var status: Int = Status.FAILED
    override var msg: String? = null
    override var users: ArrayList<AttendeeBean>? = null

    private var getUsersHttp: GetAttendeeHttpFactory? = null



    override fun loadUsers(token: String, callBack: IBaseModel.IModelCallBack) {
        if (getUsersHttp == null) {
            getUsersHttp = GetAttendeeHttpFactory()
            getUsersHttp?.setHttpEventHandler(object : HttpEventHandler<GetAttendeeBean>() {
                override fun HttpSucessHandler(result: GetAttendeeBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        users = result?.data as ArrayList<AttendeeBean>
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

    override fun fullInviteUser(): List<AttendeeBean> {
        if (inviteUser == null || inviteUser?.size == 0) {
            return users!!
        }
        var invite: ParticipantBean? = null
        var user: AttendeeBean? = null
        val checkinviteUser = ArrayList<ParticipantBean>()
        for (i in 0..inviteUser?.size!!-1) {
            invite = inviteUser?.get(i)
            for (j in 0..users!!.size-1) {
                user = users?.get(j)
                if (invite?.id.equals(user?.uid)) {
                    user?.invited = true
                    user?.cantchange = true
                    checkinviteUser.add(inviteUser?.get(i)!!)
                }
            }
        }
        inviteUser = checkinviteUser
        return users!!
    }

    override fun removeMySelf(uid: String) {
        var index = -1
        var user: AttendeeBean? = null
        for (i in 0..users?.size!!-1) {
            user = users?.get(i)
            if (user?.uid.equals(uid)) {
                index = i
                break
            }
        }
        if (index != -1){
            this.users?.removeAt(index)
        }
    }
}