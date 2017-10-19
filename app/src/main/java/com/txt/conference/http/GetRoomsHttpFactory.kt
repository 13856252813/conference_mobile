package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.txt.conference.bean.GetRoomBean
import com.txt.conference.bean.RoomBean

/**
 * Created by jane on 2017/10/12.
 */
class GetRoomsHttpFactory : HttpStringFactoryBase<GetRoomBean>() {
    val TAG = GetRoomsHttpFactory::class.java.simpleName

    override fun AnalysisData(content: String?): GetRoomBean {
        ULog.d(TAG, "content $content")
        return Gson().fromJson(content, GetRoomBean::class.java)
    }

    /**
     * index
     * 0 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.GET_ROOMS, args[0])
    }

}