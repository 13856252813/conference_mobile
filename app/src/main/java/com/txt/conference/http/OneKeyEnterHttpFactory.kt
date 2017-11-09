package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.bean.GetOneKeyEnterBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.util.ArrayList

/**
 * Created by pc on 2017/11/9.
 */
class OneKeyEnterHttpFactory : HttpStringFactoryBase<GetOneKeyEnterBean>() {
    val TAG = OneKeyEnterHttpFactory::class.java.simpleName
    var roomNo: String? = null
    var userName: String? = null

    override fun AnalysisData(content: String?): GetOneKeyEnterBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, GetOneKeyEnterBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        return Urls.ONEKEYENTER_ROOM
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {

        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("roomno", roomNo))
        body.add(BasicNameValuePair("username", userName))
        return body
    }
}