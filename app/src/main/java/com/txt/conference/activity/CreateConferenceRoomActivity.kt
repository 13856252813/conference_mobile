package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.txt.conference.R
import com.txt.conference.adapter.CreateRoomListAdapter
import com.txt.conference.bean.CreateRoomListAdapterBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.CreateConferencePresenter
import com.txt.conference.presenter.CreateConferenceRoomPresenter
import com.txt.conference.utils.Constants
import com.txt.conference.utils.CostTimePickDialogUtil
import com.txt.conference.utils.DateTimePickDialogUtil
import com.txt.conference.view.ICreateConferenceRoomView
import com.txt.conference.view.ICreateConferenceView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by pc on 2017/10/13.
 */


class CreateConferenceRoomActivity : ICreateConferenceRoomView, /*IGetUsersView,*/ DateTimePickDialogUtil.ITimePickDialogClick, CostTimePickDialogUtil.ICostTimePickDialogClick, ICreateConferenceView, BaseActivity() {

    override fun jumpActivity(roomBean: RoomBean) {
        //onBackPressed()
        var i = Intent(this, CreateConferenceFinishedActivity::class.java)
        i.putExtra(CreateConferenceFinishedActivity.KEY_ROOM, roomBean)
        startActivity(i)
        this.finish()
    }

    override fun jumpActivity() {
        onBackPressed()
    }

    override fun back() {

    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun hideError() {

    }

    companion object {
        var REQUEST_CODE_CHOOSE_ATTEND = 10
        var REQUEST_CODE_CHOOSE_DEVICE = 11

        var ITEM_TITLE = 0
        var ITEM_ATTEND = 1
        var ITEM_DEVICE = 2
        var ITEM_STARTTIME = 3
        var ITEM_COSTTIME = 4
    }

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

    var namedevicelist: ArrayList<String>? = null
    var displaydevicelist: ArrayList<String>? = null

    var mCostTime: String? = "1"
    var mStartTime: String? = ""
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
    private fun startDateTimer(){
        var datetimepick = DateTimePickDialogUtil(this, "")
        datetimepick.setTimePickeristener(this)
        datetimepick.dateTimePicKDialog()
    }

    fun startChooseAttand(){
        var i = Intent(this, ChooseManActivity::class.java)
        //var requestCode: Int = 10
        startActivityForResult(i, REQUEST_CODE_CHOOSE_ATTEND)
    }

    fun startChooseDeviceAttand(){
        var i = Intent(this, ChooseDeviceActivity::class.java)
        //var requestCode: Int = 11
        startActivityForResult(i, REQUEST_CODE_CHOOSE_DEVICE)
    }

    fun startEditTitle(){
        listadapter!!.updateItemStr(true)
        listadapter!!.notifyDataSetChanged()
    }

    fun startCostTime(){
        var costtimepick: CostTimePickDialogUtil = CostTimePickDialogUtil(this)
        costtimepick.setCostTimePickeristener(this)
        costtimepick.costTimePicKDialog()
    }

    fun createRoomJsonString (): String? {
        var title = ""
        if (listadapter?.editText != null){
            title = listadapter?.editText!!
        }
        var jsonTime: JSONObject = JSONObject()
        var jsonObj: JSONObject = JSONObject()
        var pararray: JSONArray = JSONArray()
        var namearray: JSONArray = JSONArray()
        jsonObj.put("topic", title)     // title
        jsonObj.put("duration", mCostTime)  // time
        jsonTime.put("year", Constants.TimeStrGetYear(mStartTime))
        jsonTime.put("month", Constants.TimeStrGetMonth(mStartTime))
        jsonTime.put("day", Constants.TimeStrGetDay(mStartTime))
        jsonTime.put("hour",Constants.TimeStrGetHour(mStartTime))
        jsonTime.put("min",Constants.TimeStrGetMin(mStartTime))
        jsonObj.put("start", jsonTime) // start time
        if (namelist != null ) {
            val num = namelist?.size!!
            var i = 0
            while (i < num){
                namearray.put(displaylist?.get(i))
                pararray.put(namelist?.get(i))
                i++
            }
        }

        if (namedevicelist != null ) {
            val num = namedevicelist?.size!!
            var i = 0
            while (i < num){
                namearray.put(displaydevicelist?.get(i))
                pararray.put(namedevicelist?.get(i))
                i++
            }
        }
        jsonObj.put("names", namearray)
        jsonObj.put("participants", pararray)

        return jsonObj?.toString()
    }

    fun initView() {
        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        var titlebar_title: TextView = this.findViewById<TextView>(R.id.title)
        var btn_create: Button = this.findViewById<Button>(R.id.bt_createroom)
        titlebar_back.setClickable(true)
        btn_create.setOnClickListener {

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val curNowDate = Date(System.currentTimeMillis())//获取当前时间
            val strNow :String = formatter.format(curNowDate)
            if ((mStartTime?.compareTo(strNow)!! < 0)){
                Toast.makeText(this, this.getString(R.string.starttime_before_nowtime), Toast.LENGTH_SHORT).show()
            } else {
                mCreateRoomPresenter?.doCreate(createRoomJsonString(), getToken())
            }


        }
        titlebar_back.setOnClickListener({ this.onBackPressed()/*Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()*/ })


        listview= this.findViewById<ListView>(R.id.listCreateRoomView)
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            listadapter!!.updateItemStr(false)
            when(i){
                0 -> this.startEditTitle()
                1 -> this.startChooseAttand()
                2 -> this.startChooseDeviceAttand()
                3 -> this.startDateTimer()
                4 -> this.startCostTime()

            }
        }

        mPresenter = CreateConferencePresenter(this)
        mCreateRoomPresenter = CreateConferenceRoomPresenter(this)
        mPresenter?.initListData()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val curDate = Date(System.currentTimeMillis())//获取当前时间

        mStartTime = formatter.format(curDate)



    }

    override fun onConfirm(str: String?) {

        if (listadapter != null) {
            mStartTime = str
            listadapter!!.updateItemStr(ITEM_STARTTIME, str?.substring(5, str.length))
            listadapter!!.notifyDataSetChanged()
        }


        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show()

    }

    override fun onCancel() {

    }

    override fun onCostTimeConfirm(str: String?) {
        if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_COSTTIME, str)
            mCostTime = str
            listadapter!!.notifyDataSetChanged()
        }
    }

    override fun onCostTimeCancel() {

    }

    fun onAttandManUpdate(str: String?) {
        if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_ATTEND, str)
            listadapter!!.notifyDataSetChanged()
        }
    }

    fun onDeviceManUpdate(str: String?) {
        if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_DEVICE, str)
            listadapter!!.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //Log.i("mytest3", requestCode.toString() + ":" + resultCode.toString())

        if (resultCode != 0) {
            return
        }

        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE_ATTEND) {
                namelist = data?.getStringArrayListExtra("nameattandList")
                displaylist = data?.getStringArrayListExtra("displayattandList")
                onAttandManUpdate(namelist?.size.toString())
            } else if (requestCode == REQUEST_CODE_CHOOSE_DEVICE){
                namedevicelist = data?.getStringArrayListExtra("nameattandList")
                displaydevicelist = data?.getStringArrayListExtra("displayattandList")
                onDeviceManUpdate(namedevicelist?.size.toString())
            }
        }


    }
}