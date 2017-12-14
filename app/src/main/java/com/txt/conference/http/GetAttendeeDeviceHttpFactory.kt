package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.google.gson.Gson
import com.txt.conference.bean.GetAttendeeBean

/**
 * Created by pc on 2017/11/07.
 */
class GetAttendeeDeviceHttpFactory : HttpStringFactoryBase<GetAttendeeBean>() {

    override fun AnalysisData(content: String?): GetAttendeeBean {
        return Gson().fromJson(content, GetAttendeeBean::class.java)
    }

    /**
     * 0 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.GET_USERS_DEVICE, args[0])
    }
}