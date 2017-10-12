package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import cn.hugeterry.updatefun.UpdateFunGO
import cn.hugeterry.updatefun.config.UpdateKey
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.data.TxSharedPreferencesFactory

/**
 * Created by jane on 2017/10/9.
 */
class WelcomeActivity : BaseActivity() {
    val JUMP = 1
    val JUMP_TIME = 3000L

    var mHandle = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                JUMP -> {
                    var token = TxSharedPreferencesFactory(TxApplication.mInstance).getToken()
                    var i = Intent(this@WelcomeActivity, MainActivity::class.java)
                    if (token == null || token.equals("")){
                        i = Intent(this@WelcomeActivity, LoginActivity::class.java)
                    }
                    this@WelcomeActivity.startActivity(i)
                    this@WelcomeActivity.finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initFirUpdate()
        printDisplayMetrics()
    }

    private fun initFirUpdate() {
        UpdateKey.API_TOKEN = "ee20eb2948374c12e9f993cec1507955"
        UpdateKey.APP_ID = "59dc7030548b7a481b00036c"
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG
        UpdateFunGO.init(this)
    }

    override fun onResume() {
        super.onResume()
        mHandle.sendEmptyMessageDelayed(JUMP, JUMP_TIME)
        UpdateFunGO.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        mHandle.removeMessages(JUMP)
        UpdateFunGO.onStop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun printDisplayMetrics(){
        //代码中获取信息
        var metric = DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(metric)
        var width = metric.widthPixels  // 屏幕宽度（像素）
        var height = metric.heightPixels  // 屏幕高度（像素）
        var density = metric.density  // 屏幕密度（0.75 / 1.0 / 1.5）
        var densityDpi = metric.densityDpi  // 屏幕密度DPI（120 / 160 / 240）=(屏幕密度*160)
        var dpi = getDpiStr(densityDpi)


        Log.d("Jane", "屏幕宽度:"+width);
        Log.d("Jane", "屏幕高度:"+height);
        Log.d("Jane", "屏幕密度:"+density);
        Log.d("Jane", "屏幕密度DPI:"+densityDpi);
        Log.d("Jane", "屏幕密度DPI:"+dpi);
    }

    fun getDpiStr(dpi: Int): String{
        var dpiStr = "null";
        if (dpi <= 120){
            dpiStr = "null";
        }else if (dpi <= 160){
            dpiStr = "mdpi";
        }else if (dpi <= 240){
            dpiStr = "hdpi";
        }else if (dpi <= 320){
            dpiStr = "xhdpi";
        }else if (dpi <= 480){
            dpiStr = "xxhdpi";
        }else if (dpi <= 640){
            dpiStr = "xxxhdpi";
        }else{
            dpiStr = "null";
        }
        return dpiStr;
    }
}