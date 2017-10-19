package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceUserAdapter
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.view.IGetUsersView
import com.txt.conference.presenter.GetUsersPresenter
/**
 * Created by pc on 2017/10/15.
 */


class ChooseManActivity : IGetUsersView, View.OnClickListener, BaseActivity() {

    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun jumpActivity() {

    }

    var getuserPresenter: GetUsersPresenter? = null
    var listview: ListView? = null
    var listadapter: ConferenceUserAdapter? = null
    var titlebar_back: TextView? = null
    var titlebar_title: TextView? = null
    var titlebar_finish: TextView? = null
    override fun addAttendees(conference: List<AttendeeBean>?) {

        var num: Int = conference?.size!!
        val bool_array = arrayOfNulls<Boolean>(conference?.size!!)
        var i = 0
        while (i < num){
            bool_array[i] = false
            i++
        }

        if (listadapter == null) {
            listadapter = ConferenceUserAdapter(conference, bool_array, this)
            listview?.setAdapter(listadapter)
        }
        updateTitleBar()
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

    fun updateTitleBar() {
        titlebar_title?.text = this.resources.getString(R.string.add_attendee) + "(" +  listadapter?.getCheckedNum().toString() +")"

    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.left_attandback_text -> {
                onBackPressed()
            }
            R.id.right_attandfinish_text -> {

            }
        }
    }

    fun initView() {
        var check: Boolean? = null
        titlebar_back = this.findViewById<TextView>(R.id.left_attandback_text)
        titlebar_finish = this.findViewById<TextView>(R.id.right_attandfinish_text)
        titlebar_title = this.findViewById<TextView>(R.id.title_attand)
        listview= this.findViewById<ListView>(R.id.listUserView)
        titlebar_back?.setOnClickListener(this)
        titlebar_finish?.setOnClickListener(this)
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            if (listadapter!!.getItemCheck(i) == true){
                check = false
            } else {
                check = true
            }
            listadapter?.setItemCheck(i, check)
            Log.i("mytest", i.toString())
            listadapter!!.notifyDataSetChanged()
            updateTitleBar()
        }
        getuserPresenter = GetUsersPresenter(this)
        getuserPresenter?.getUsers(getToken())
        updateTitleBar()
    }

}