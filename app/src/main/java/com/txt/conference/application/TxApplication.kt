package com.txt.conference.application

import android.content.Context
import com.common.application.BaseApplication

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
    }
}