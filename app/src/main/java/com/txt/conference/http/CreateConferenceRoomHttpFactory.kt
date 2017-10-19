package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.CreateConferenceRoomBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
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
        return Urls.CREATE_ROOM
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {

        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("duration", "10"))
        body.add(BasicNameValuePair("names", "[\"abc\", \"ddd\"]"))
        body.add(BasicNameValuePair("participants", "[\"account\\abc\", \"account\\ddd\"]"))
        body.add(BasicNameValuePair("start", "{year: 2017, month: 10, day: 19, hour: 16, min: 4}"))
        body.add(BasicNameValuePair("topic", "dfdddd"))
        return body
    }
}