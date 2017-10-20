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
import com.txt.conference.utils.CostTimePickDialogUtil
import com.txt.conference.utils.DateTimePickDialogUtil
import com.txt.conference.view.ICreateConferenceRoomView
import com.txt.conference.view.ICreateConferenceView
import com.txt.conference.view.IGetUsersView

/**
 * Created by pc on 2017/10/13.
 */


class CreateConferenceRoomActivity : ICreateConferenceRoomView, /*IGetUsersView,*/ DateTimePickDialogUtil.ITimePickDialogClick, CostTimePickDialogUtil.ICostTimePickDialogClick, ICreateConferenceView, BaseActivity() {
    override fun jumpActivity(loginBean: LoginBean) {

    }

    override fun jumpActivity() {

    }

    override fun back() {

    }

    override fun showError(error: String) {

    }

    override fun hideError() {

    }


    /*override fun jumpActivity() {

    }

    override fun addAttendees(conference: List<AttendeeBean>?) {
        userNum = conference?.size.toString()
        if (listadapter != null) {
            listadapter!!.updateItemStr(0, userNum)
            listadapter!!.notifyDataSetChanged()
        }
    }

    override fun back() {

    }

    override fun jumpToLogin() {

    }*/

    //var mPresenter: CreateConferencePresenter()
    var mPresenter: CreateConferencePresenter? = null
    var mCreateRoomPresenter: CreateConferenceRoomPresenter? = null
    var listview: ListView? = null
    var listadapter: CreateRoomListAdapter? = null
    //var getuserPresenter: GetUsersPresenter? = null
    var userNum: String? = null

    //var mAttandList: Array<AttendeeBean>? = null
    var namelist: ArrayList<String>? = null
    var displaylist: ArrayList<String>? = null

    override fun initListViewData(listdata: ArrayList<CreateRoomListAdapterBean>) {
        listadapter = CreateRoomListAdapter(listdata, this)
        listview?.setAdapter(listadapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createconferenceroom)
        initView()
    }

    fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }
    fun startDateTimer(){
        var datetimepick: DateTimePickDialogUtil = DateTimePickDialogUtil(this, "")
        datetimepick.setTimePickeristener(this)
        datetimepick.dateTimePicKDialog()
    }

    fun startChooseAttand(){
        var i = Intent(this, ChooseManActivity::class.java)
        var requestCode: Int = 10
        startActivityForResult(i, requestCode)
    }

    fun startCostTime(){
        var costtimepick: CostTimePickDialogUtil = CostTimePickDialogUtil(this)
        costtimepick.setCostTimePickeristener(this)
        costtimepick.costTimePicKDialog()
    }


    fun initView() {
        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        var titlebar_title: TextView = this.findViewById<TextView>(R.id.title)
        var btn_create: Button = this.findViewById<Button>(R.id.bt_createroom)
        titlebar_back.setClickable(true)
        btn_create.setOnClickListener {
            Log.i("mytest", "create")
            mCreateRoomPresenter?.doCreate(getToken())
        }
        titlebar_back.setOnClickListener({ this.onBackPressed()/*Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()*/ })


        listview= this.findViewById<ListView>(R.id.listView)
        listview?.setOnItemClickListener { adapterView, view, i, l ->

            when(i){
                0 -> this.startChooseAttand()

                1 -> this.startDateTimer()
                2 -> this.startCostTime()

            }
        }

        mPresenter = CreateConferencePresenter(this)
        mCreateRoomPresenter = CreateConferenceRoomPresenter(this)
        mPresenter?.initListData()

        //getuserPresenter = GetUsersPresenter(this)
        //getuserPresenter?.getUsers(getToken())


    }

    override fun onConfirm(str: String?) {

        if (listadapter != null) {
            listadapter!!.updateItemStr(1, str)
            listadapter!!.notifyDataSetChanged()
        }


        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()

    }

    override fun onCancel() {

    }

    override fun onCostTimeConfirm(str: String?) {
        if (listadapter != null) {
            listadapter!!.updateItemStr(2, str)
            listadapter!!.notifyDataSetChanged()
        }
    }

    override fun onCostTimeCancel() {

    }

    fun onAttandManUpdate(str: String?) {
        if (listadapter != null) {
            listadapter!!.updateItemStr(0, str)
            listadapter!!.notifyDataSetChanged()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i("mytest3", requestCode.toString() + ":" + resultCode.toString())

        if (resultCode != 0) {
            return
        }
        if (data != null) {
            namelist  = data?.getStringArrayListExtra("nameattandList")
            displaylist = data?.getStringArrayListExtra("displayattandList")
            Log.i("mytest2", namelist?.size.toString())
            onAttandManUpdate(namelist?.size.toString())
        }


    }
}