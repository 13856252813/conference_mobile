package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceUserAdapter
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.GetUserDevicePresenter
import com.txt.conference.view.IGetUsersView
import com.txt.conference.presenter.GetUsersPresenter
/**
 * Created by pc on 2017/11/07.
 */


class ChooseDeviceActivity : IGetUsersView, View.OnClickListener, BaseActivity() {
    override fun getUid(): String? {
        return ""
    }

    override fun setAttendeeNumber(number: Int) {

    }

    override fun setAttendeeAllNumber(number: Int) {

    }

    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun jumpActivity() {

    }

    companion object {
        var KEY_ROOM = "room_key"
    }
    var getuserPresenter: GetUserDevicePresenter? = null
    var listview: ListView? = null
    var listadapter: ConferenceUserAdapter? = null
    var titlebar_back: TextView? = null
    var titlebar_title: TextView? = null
    var titlebar_finish: TextView? = null

    var room: RoomBean? = null
    val requestCode: Int = 0


    override fun addAttendees(conference: List<AttendeeBean>?) {

        var num: Int = conference?.size!!


        val bool_array = arrayOfNulls<Boolean>(num)
        val conflist  = java.util.ArrayList<AttendeeBean>()
        var i = 0

        while (i < conference?.size!!){
            if (!(conference.get(i).uid.equals(getUserUid()))) {
                conflist.add(conference?.get(i))
            }
            i++
        }

        for (j in conflist.indices){
            bool_array[j] = false
            if (room != null) {
                for (k in room?.participants!!.indices) {
                    if (room?.participants!!.get(k).id!!.equals(conflist.get(j).uid)) {
                        bool_array[j] = true
                        break
                    }
                }
            }
        }


        if (listadapter == null) {
            listadapter = ConferenceUserAdapter(conflist, bool_array, this)
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

    fun getUserUid():String?{
        return TxSharedPreferencesFactory(applicationContext).getId()
    }

    fun updateTitleBar() {
        titlebar_title?.text = this.resources.getString(R.string.add_attendee) + "(" +  listadapter?.getCheckedNum().toString() +")"

    }

    fun onFinished() {
        var resultCode: Int  = 0

        var mIntent: Intent  = Intent()

        var nameattandlist: ArrayList<String>? = ArrayList<String>()
        var displayattandlist: ArrayList<String>? = ArrayList<String>()
        if (listadapter?.getCheckedList() == null){

            resultCode = 1
            this.setResult(resultCode, mIntent)
        }


        var checkedlist: ArrayList<AttendeeBean> = listadapter?.getCheckedList()!!

        var i : Int = 0
        while ( i < checkedlist.size){
            nameattandlist?.add(checkedlist[i].uid!!)
            displayattandlist?.add(checkedlist[i].display!!)
            i++
        }

        mIntent.putStringArrayListExtra("nameattandList", nameattandlist);
        mIntent.putStringArrayListExtra("displayattandList",displayattandlist);
        this.setResult(resultCode, mIntent)

        this.finish()
    }
    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.left_attandback_text -> {
                onBackPressed()
            }
            R.id.right_attandfinish_text -> {
                onFinished()
            }
        }
    }

    fun initView() {
        var check: Boolean? = null
        titlebar_back = this.findViewById<TextView>(R.id.left_attandback_text)
        titlebar_finish = this.findViewById<TextView>(R.id.right_attandfinish_text)
        titlebar_title = this.findViewById<TextView>(R.id.title_attand)
        listview= this.findViewById<ListView>(R.id.listUserView)

        if (intent.getSerializableExtra(KEY_ROOM) != null) {
            room = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        }
        titlebar_back?.setOnClickListener(this)
        titlebar_finish?.setOnClickListener(this)
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            if (listadapter!!.getItemCheck(i) == true){
                check = false
            } else {
                check = true
            }
            listadapter?.setItemCheck(i, check)
            listadapter!!.notifyDataSetChanged()
            updateTitleBar()
        }
        getuserPresenter = GetUserDevicePresenter(this)
        getuserPresenter?.getUsers(getToken())
        updateTitleBar()
    }

}