package com.txt.conference.activity

import android.os.Bundle
import android.view.View
import com.txt.conference.R

import kotlinx.android.synthetic.main.activity_onekeyenter.*

/**
 * Created by pc on 2017/11/08.
 */
class OneKeyEnterActivity : BaseActivity(), View.OnClickListener {

    override fun jumpActivity() {

    }

    override fun back() {

    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.enter_bt_enter -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onekeyenter)

        enter_bt_enter.setOnClickListener(this)
    }
}