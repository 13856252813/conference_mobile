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
    var mconferenceRoomUser: CreateConferenceRoomBean? = null

    override fun AnalysisData(content: String?): CreateConferenceRoomBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, CreateConferenceRoomBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.CREATE_ROOM, args[0])
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {

        var pararray: JSONArray = JSONArray()
        pararray.put("aaccount\\abc\\")
        pararray.put("account\\ddd\\")

        var jsonTime: JSONObject = JSONObject()
        jsonTime.put("year", 2017)
        jsonTime.put("month", 10)
        jsonTime.put("day", 20)
        jsonTime.put("hour", 16)
        jsonTime.put("min", 10)
        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("duration", "10"))
        body.add(BasicNameValuePair("names", "[]"))
        body.add(BasicNameValuePair("participants","[]"))
        Log.i("mytest", jsonTime.toString())
        body.add(BasicNameValuePair("start", jsonTime.toString()))
        body.add(BasicNameValuePair("topic", "dfdddd"))

        return body
    }

    override fun getPostArgsJsonStr(): String {
        //return super.getPostArgsJsonStr()
        var jsonObj: JSONObject = JSONObject()
        var jsonTime: JSONObject = JSONObject()
        var pararray: JSONArray = JSONArray()
        var namearray: JSONArray = JSONArray()
        pararray.put("account\\abc")
        pararray.put("account\\ddd")
        namearray.put("abc")
        namearray.put("ddd")
        jsonTime.put("year", 2017)
        jsonTime.put("month", 10)
        jsonTime.put("day", 20)
        jsonTime.put("hour", 16)
        jsonTime.put("min", 10)
        jsonObj.put("duration", "3")
        jsonObj.put("topic", "dfdddd")
        jsonObj.put("names", namearray)
        jsonObj.put("participants", pararray)
        jsonObj.put("start", jsonTime)

        //var str: String  = "{\"topic\":\"\",\"start\":{\"year\":2017,\"month\":10,\"day\":20,\"hour\":16,\"min\":15},\"duration\":1,\"participants\":[],\"names\":[]}"
        //return str
        return jsonObj.toString()
    }
}