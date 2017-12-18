package com.txt.conference.http

import android.text.TextUtils
import android.util.Log
import com.common.http.HttpStringFactoryBase
import com.common.utlis.ULog
import com.common.utlis.URLEncoderUtils
import com.google.gson.Gson
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.model.ClientModel
import com.txt.conference.model.MediaModel
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import java.util.ArrayList

import java.util.HashMap

/**
 * Created by pc on 2017/12/13.
 */

class ControlMediaFactory(private var map: HashMap<String, String>) : HttpStringFactoryBase<MediaModel>() {

    val TAG = ControlMediaFactory::class.java.simpleName
    override fun getPostArgs(): ArrayList<NameValuePair> {
        var list= ArrayList<NameValuePair>()
        if(map != null && map.size>0){
            for ((key,value) in map){
                val nameValuePair = BasicNameValuePair(key, value)
                list.add(nameValuePair)
            }
        }
        return list
    }

    override fun AnalysisData(content: String?): MediaModel? {
        ULog.d(TAG, "content $content")
        var bean = Gson().fromJson(content, MediaModel::class.java)
        return bean
    }

    override fun CreateUri(vararg args: Any?): String? {
        return String.format(Urls.MEDIA_CONTROL, URLEncoderUtils.encode(args[0] as String), URLEncoderUtils.encode(args[1] as String),args[2])
    }
}
