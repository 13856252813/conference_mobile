package com.txt.conference.http

import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.google.gson.Gson
import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.UserBean
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by jane on 2017/10/12.
 */
class LoginHttpFactory : HttpStringFactoryBase<LoginBean>() {
    val TAG = LoginHttpFactory::class.java.simpleName
    var mUser: UserBean? = null

    override fun AnalysisData(content: String?): LoginBean {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, LoginBean::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String {
        return Urls.LOGIN
    }

    override fun getPostArgs(): ArrayList<org.apache.http.NameValuePair> {

        var body = ArrayList<NameValuePair>()
        body.add(BasicNameValuePair("username", mUser?.account))
        body.add(BasicNameValuePair("password", mUser?.password))
        body.add(BasicNameValuePair("device", "android"))
        return body
    }

    override fun getPostArgsJsonStr(): String {

        var jsonObj: JSONObject = JSONObject()
        jsonObj.put("username", mUser?.account)
        jsonObj.put("password", mUser?.password)
        jsonObj.put("device", "android")
        //return super.getPostArgsJsonStr()
        return jsonObj.toString()
    }
}