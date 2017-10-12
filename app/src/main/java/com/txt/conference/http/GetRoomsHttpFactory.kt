package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.google.gson.Gson
import com.txt.conference.bean.LoginBean

/**
 * Created by jane on 2017/10/12.
 */
class GetRoomsHttpFactory : HttpStringFactoryBase<LoginBean.RoomEntity>() {
    override fun AnalysisData(content: String?): LoginBean.RoomEntity {
        return Gson().fromJson(content, LoginBean.RoomEntity::class.java)
    }

    /**
     * index
     * 0 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.GET_ROOMS, args[0])
    }

}