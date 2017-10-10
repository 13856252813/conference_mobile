package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import com.txt.conference.R

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
                    var i = Intent(this@WelcomeActivity, MainActivity::class.java)
                    this@WelcomeActivity.startActivity(i)
                    this@WelcomeActivity.finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        printDisplayMetrics()
    }

    override fun onResume() {
        super.onResume()
        mHandle.sendEmptyMessageDelayed(JUMP, JUMP_TIME)
    }

    override fun onPause() {
        super.onPause()
        mHandle.removeMessages(JUMP)
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