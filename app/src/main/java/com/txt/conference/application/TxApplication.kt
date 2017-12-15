package com.txt.conference.application

import android.content.Context
import android.widget.Toast
import cn.jpush.android.api.JPushInterface
import com.common.application.BaseApplication
import com.txt.conference.utils.ToastUtils

/**
 * Created by jane on 2017/10/12.
 */
class TxApplication : BaseApplication() {

    companion object {
        var mInstance: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = applicationContext

        ToastUtils.init(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}