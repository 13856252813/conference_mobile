package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.txt.conference.R

import com.txt.conference.adapter.CreateRoomListAdapter
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.CreateRoomListAdapterBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.CreateConferencePresenter
import com.txt.conference.presenter.CreateConferenceRoomPresenter
import com.txt.conference.presenter.GetUsersPresenter
import com.txt.conference.utils.Constants
import com.txt.conference.utils.CostTimePickDialogUtil
import com.txt.conference.utils.DateTimePickDialogUtil
import com.txt.conference.view.ICreateConferenceRoomView
import com.txt.conference.view.ICreateConferenceView
import com.txt.conference.view.IGetUsersView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pc on 2017/11/7.
 */


class CreateConferenceFinishedActivity : BaseActivity() {
    override fun jumpActivity() {

    }

    override fun back() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createconferencefinished)
        initView()
    }

    fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }


    fun initView() {
        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        var titlebar_title: TextView = this.findViewById<TextView>(R.id.title)
        var btn_create: Button = this.findViewById<Button>(R.id.bt_createroom)
        titlebar_back.setClickable(true)

    }

}