package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.GetAttendeeBean
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
}