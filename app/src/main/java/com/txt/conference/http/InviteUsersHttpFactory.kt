package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.common.utlis.URLEncoderUtils
import com.google.gson.Gson
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.InviteUsersBean
import com.txt.conference.bean.LogoffBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.ArrayList

/**
 * Created by jane on 2017/10/12.
 */
class InviteUsersHttpFactory : HttpStringFactoryBase<InviteUsersBean>() {
    val TAG = InviteUsersHttpFactory::class.java.simpleName
    var token: String? = null
    var attendeeList: List<AttendeeBean>? = null
    var attendType = 0

    override fun AnalysisData(content: String?): InviteUsersBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, InviteUsersBean::class.java)
        return bean
    }

    /**
     * 0 roomId
     * 1 token
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.INVItE_USERS, URLEncoderUtils.encode(args[0] as String), args[1])
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {
        var body = ArrayList<NameValuePair>()
//        if (attendeeList == null) {
//            return body
//        }
//        var part = ArrayList<String>()
//        var names = ArrayList<String>()
//        for (i in 0..attendeeList?.size!!-1) {
//            part.add(attendeeList?.get(i)?.uid!!)
//            names.add(attendeeList?.get(i)?.display!!)
//        }
//        body.add(BasicNameValuePair("part", Gson().toJson(part)))
//        body.add(BasicNameValuePair("names", Gson().toJson(names)))
        return body
    }

    override fun getPostArgsJsonStr(): String {
        var jsonObj: JSONObject = JSONObject()
        var paritinarray = JSONArray()

        /*var part = JSONArray()
        var names = JSONArray()
        for (i in 0..attendeeList?.size!!-1) {
            part.put(attendeeList?.get(i)?.id!!)
            names.put(attendeeList?.get(i)?.display!!)
        }
        jsonObj.put("part", part)
        jsonObj.put("names", names)*/


        if (attendeeList != null ) {
            val num = attendeeList!!.size!!
            var i = 0
            while (i < num){
                var userjsonObj: JSONObject = JSONObject()
                userjsonObj.put("id", attendeeList!!.get(i).id)
                userjsonObj.put("name", attendeeList!!.get(i).display)
                userjsonObj.put("mobile", attendeeList!!.get(i).mobile)
                userjsonObj.put("email", attendeeList!!.get(i).email)
                if (attendType == 0) {
                    userjsonObj.put("group", "account")
                } else {
                    userjsonObj.put("group", "device")
                }
                paritinarray.put(userjsonObj)
                //namearray.put(displaylist?.get(i))
                //pararray.put(namelist?.get(i))
                i++
            }
        }
        jsonObj.put("part", paritinarray)
        ULog.d(TAG, "getPostArgsJsonStr:" + jsonObj.toString())
        return jsonObj.toString()
    }
}