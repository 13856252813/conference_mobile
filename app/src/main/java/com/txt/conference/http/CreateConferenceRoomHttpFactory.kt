package com.txt.conference.http

import android.util.Log
import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.CreateConferenceRoomBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by pc on 2017/10/19.
 */
class CreateConferenceRoomHttpFactory : HttpStringFactoryBase<CreateConferenceRoomBean>() {
    val TAG = CreateConferenceRoomHttpFactory::class.java.simpleName

    var mPostCreaetJsonStr: String? = null

    override fun AnalysisData(content: String?): CreateConferenceRoomBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, CreateConferenceRoomBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        Log.i("CreateUri:", Urls.CREATE_ROOM)
        return String.format(Urls.CREATE_ROOM, args[0])
    }

    override fun getPostArgs(): ArrayList<NameValuePair> {
        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("poststr", mPostCreaetJsonStr))
        return body
    }

    override fun getPostArgsJsonStr(): String {

        /*var jsonObj: JSONObject = JSONObject()
        var jsonTime: JSONObject = JSONObject()
        var pararray: JSONArray = JSONArray()
        var namearray: JSONArray = JSONArray()
        pararray.put("account\\abc")
        pararray.put("account\\ddd")
        namearray.put("abc")
        namearray.put("ddd")
        jsonTime.put("year", 2017)
        jsonTime.put("month", 10)
        jsonTime.put("day", 21)
        jsonTime.put("hour", 22)
        jsonTime.put("min", 10)
        jsonObj.put("duration", "3")
        jsonObj.put("topic", "dfdddd")
        jsonObj.put("names", namearray)
        jsonObj.put("participants", pararray)
        jsonObj.put("start", jsonTime)
        return jsonObj.toString()*/
        Log.i("mPostCreaetJsonStr:", mPostCreaetJsonStr)
        return mPostCreaetJsonStr!!


    }
}