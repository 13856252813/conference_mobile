package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.LogoffBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.util.ArrayList

/**
 * Created by jane on 2017/10/12.
 */
class InviteUsersHttpFactory : HttpStringFactoryBase<LogoffBean>() {
    val TAG = LogoffHttpFactory::class.java.simpleName
    var token: String? = null

    override fun AnalysisData(content: String?): LogoffBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, LogoffBean::class.java)
        return bean
    }

    /**
     * 0 
     */
    override fun CreateUri(vararg args: Any?): String {
        return String.format(Urls.INVItE_USERS, token)
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {
        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("token", token))
        return body
    }
}