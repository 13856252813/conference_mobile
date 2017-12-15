package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.common.utlis.URLEncoderUtils
import com.google.gson.Gson
import com.txt.conference.bean.CreateConferenceRoomBean


/**
 * Created by pc on 2017/11/02.
 */
class DeleteRoomUserHttpFactory : HttpStringFactoryBase<CreateConferenceRoomBean>() {
    val TAG = DeleteRoomUserHttpFactory::class.java.simpleName

    override fun AnalysisData(content: String?): CreateConferenceRoomBean {
        ULog.d(TAG, "content $content")
        return Gson().fromJson(content, CreateConferenceRoomBean::class.java)
    }

    /**
     * index
     * 0 roomid
     * 1 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.DELETE_ROOM_USER, URLEncoderUtils.encode(args[0] as String), args[1], args[2])
    }

    override fun IfDeleteRequest(): Boolean {
        return true
    }
}