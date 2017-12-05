package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.data.TxSharedPreferencesFactory
import kotlinx.android.synthetic.main.activity_userguide.*

/**
 * Created by pc on 2017/11/29.
 */
class UserGuideActivity : BaseActivity(), View.OnClickListener {

    override fun jumpActivity() {

    }

    override fun back() {

    }

    fun jumpLogin() {
        var i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    fun jumpFaceLoginSetting() {
        var i = Intent(this, LoginFaceSettingsActivity::class.java)
        startActivity(i)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.firstsetting_bt_setting -> {
                jumpFaceLoginSetting()
            }

            R.id.login_bt_skip-> {
                jumpLogin()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userguide)

        login_bt_skip.setOnClickListener(this)
        firstsetting_bt_setting.setOnClickListener(this)

        TxSharedPreferencesFactory(TxApplication.mInstance).setFirstRun(false)
    }


}