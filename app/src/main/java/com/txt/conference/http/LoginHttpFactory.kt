package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.GetLoginBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.util.ArrayList

/**
 * Created by jane on 2017/10/12.
 */
class LoginHttpFactory : HttpStringFactoryBase<GetLoginBean>() {
    val TAG = LoginHttpFactory::class.java.simpleName
    var account: String? = null
    var password: String? = null

    override fun AnalysisData(content: String?): GetLoginBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, GetLoginBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        return Urls.LOGIN
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {

        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("username", account))
        body.add(BasicNameValuePair("password", password))
        body.add(BasicNameValuePair("device", "android"))
        return body
    }
}