package com.txt.conference.http

import android.util.Log
import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.common.utlis.URLEncoderUtils
import com.google.gson.Gson
import com.txt.conference.bean.CreateConferenceRoomBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by pc on 2017/12/12.
 */
class RoomExtendHttpFactory : HttpStringFactoryBase<CreateConferenceRoomBean>() {
    val TAG = RoomExtendHttpFactory::class.java.simpleName

    var mPostCreaetJsonStr: String? = null

    var extendMin: Int = 0

    override fun AnalysisData(content: String?): CreateConferenceRoomBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, CreateConferenceRoomBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        Log.i("CreateUri:", Urls.EXTEND_ROOM_TIME)
        extendMin = args[2] as Int
        return String.format(Urls.EXTEND_ROOM_TIME, URLEncoderUtils.encode(args[0] as String), args[1])
    }

    override fun getPostArgs(): ArrayList<NameValuePair> {
        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("poststr", mPostCreaetJsonStr))
        return body
    }

    override fun getPostArgsJsonStr(): String {

        var jsonObj: JSONObject = JSONObject()
        jsonObj.put("min", extendMin)
        Log.i("mPostCreaetJsonStr:", jsonObj.toString())
        return jsonObj.toString()


    }
}