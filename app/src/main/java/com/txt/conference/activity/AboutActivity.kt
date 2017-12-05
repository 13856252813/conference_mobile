package com.txt.conference.activity

import android.os.Bundle
import com.txt.conference.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.title_bar.*


/**
 * Created by pc on 2017/11/30.
 */


class AboutActivity : BaseActivity() {


    val TAG = AboutActivity::class.java.simpleName

    override fun jumpActivity() {
    }

    override fun back() {
        onBackPressed()
        finish()
    }

    fun initView() {
        about_version_id.text = packageManager.getPackageInfo(packageName, 0).versionName
        title_bar_title.text = getString(R.string.menulayout_about)
        left_text.setOnClickListener {
            back()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initView()
    }


}