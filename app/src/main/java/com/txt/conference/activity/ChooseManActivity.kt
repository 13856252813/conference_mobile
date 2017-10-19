package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceUserAdapter
import com.txt.conference.adapter.CreateRoomListAdapter
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.view.IGetUsersView
import com.txt.conference.presenter.GetUsersPresenter
/**
 * Created by pc on 2017/10/15.
 */


class ChooseManActivity : IGetUsersView, BaseActivity() {
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun jumpActivity() {

    }

    var getuserPresenter: GetUsersPresenter? = null
    var listview: ListView? = null
    var listadapter: ConferenceUserAdapter? = null

    override fun addAttendees(conference: List<AttendeeBean>?) {
        val listcheck: ArrayList<Boolean>
        var num: Int = conference.size
        for (item in num){

        }
        if (listadapter == null) {
            listadapter = ConferenceUserAdapter(conference, this)
            listview?.setAdapter(listadapter)
        }
    }

    override fun back() {

    }

    override fun jumpToLogin() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseman)
        initView()
    }

    fun initView() {
        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        var titlebar_title: TextView = this.findViewById<TextView>(R.id.title)

        listview= this.findViewById<ListView>(R.id.listUserView)
        listview?.setOnItemClickListener { adapterView, view, i, l ->

        }
        //titlebar_title.text = this.resources.getResourceName(R.string.create_room)
    }

}