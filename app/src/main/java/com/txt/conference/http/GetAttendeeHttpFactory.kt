package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.google.gson.Gson
import com.txt.conference.bean.GetAttendeeBean

/**
 * Created by jane on 2017/10/17.
 */
class GetAttendeeHttpFactory : HttpStringFactoryBase<GetAttendeeBean>() {
    override fun AnalysisData(content: String?): GetAttendeeBean {
        return Gson().fromJson(content, GetAttendeeBean::class.java)
    }

    /**
     * 0 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.GET_USERS, args[0])
    }
}