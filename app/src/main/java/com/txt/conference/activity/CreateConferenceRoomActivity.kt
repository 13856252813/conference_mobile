package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.txt.conference.R
import com.txt.conference.bean.AttendeeListBean
import com.txt.conference.bean.ConferenceUserBean
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
import com.txt.conference.widget.DialogWheelYearMonthDay
import kotlinx.android.synthetic.main.activity_createconferenceroom.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by pc on 2017/10/13.
 */
class CreateConferenceRoomActivity : ICreateConferenceRoomView, /*IGetUsersView,*/ DateTimePickDialogUtil.ITimePickDialogClick, CostTimePickDialogUtil.ICostTimePickDialogClick, ICreateConferenceView, BaseActivity() {
    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    private var mDateDialog:DialogWheelYearMonthDay?=null

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

        var KEY_ATTANDLIST = "attandlist"
        var KEY_ATTANDBEAN = "attandbean"
        var KEY_ATTANDDEVICELIST = "attanddevicelist"

        var TOPIC = "topic"
        var DURATION = "duration"
        var YEAR = "year"
        var MONTH = "month"
        var DAY = "day"
        var HOUR = "hour"
        var MIN = "min"
        var START = "start"
    }

    //var mPresenter: CreateConferencePresenter()
    var mPresenter: CreateConferencePresenter? = null
    var mCreateRoomPresenter: CreateConferenceRoomPresenter? = null
    var userNum: String? = null

    //var mAttandList: Array<AttendeeBean>? = null
    var namelist: ArrayList<String>? = null
    var displaylist: ArrayList<String>? = null

    var namedevicelist: ArrayList<String>? = null
    var displaydevicelist: ArrayList<String>? = null
    var userlistbean: ArrayList<ConferenceUserBean>? = null

    var manattendlist: AttendeeListBean? = null
    var deviceattendlist: AttendeeListBean? = null

    var mCostTime: String? = "1"
    var mStartTime: String? = ""
    override fun initListViewData(listdata: ArrayList<CreateRoomListAdapterBean>) {
        //listadapter = CreateRoomListAdapter(listdata, this)
        //listview?.adapter = listadapter
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
        if(manattendlist != null){
            i.putExtra(KEY_ATTANDBEAN,manattendlist)
        }
        //var requestCode: Int = 10
        startActivityForResult(i, REQUEST_CODE_CHOOSE_ATTEND)
    }

    fun startChooseDeviceAttand(){
        var i = Intent(this, ChooseDeviceActivity::class.java)
        //var requestCode: Int = 11
        startActivityForResult(i, REQUEST_CODE_CHOOSE_DEVICE)
    }

    fun startEditTitle(){
        //listadapter!!.updateItemStr(true)
        //listadapter!!.notifyDataSetChanged()
    }

    fun startCostTime(){
        var costtimepick: CostTimePickDialogUtil = CostTimePickDialogUtil(this)
        costtimepick.setCostTimePickeristener(this)
        costtimepick.costTimePicKDialog()
    }

    fun createRoomJsonString (): String? {
        var title = ""
        /*if (listadapter?.editText != null){
            title = listadapter?.editText!!
        }*/
        title = editviewinfo.text.toString()
        var jsonTime: JSONObject = JSONObject()
        var jsonObj: JSONObject = JSONObject()
        var pararray: JSONArray = JSONArray()
        var namearray: JSONArray = JSONArray()
        var paritinarray: JSONArray = JSONArray()
        jsonObj.put(TOPIC, title)     // title
        jsonObj.put(DURATION, mCostTime)  // time
        jsonTime.put(YEAR, Constants.TimeStrGetYear(mStartTime))
        jsonTime.put(MONTH, Constants.TimeStrGetMonth(mStartTime))
        jsonTime.put(DAY, Constants.TimeStrGetDay(mStartTime))
        jsonTime.put(HOUR,Constants.TimeStrGetHour(mStartTime))
        jsonTime.put(MIN,Constants.TimeStrGetMin(mStartTime))
        jsonObj.put(START, jsonTime) // start time
        //var userjsonObj: JSONObject = JSONObject()
        if (manattendlist != null ) {
            val num = manattendlist?.datalist!!.size!!
            var i = 0
            while (i < num){
                var userjsonObj: JSONObject = JSONObject()
                userjsonObj.put("id", manattendlist?.datalist!!.get(i).id)
                userjsonObj.put("name", manattendlist?.datalist!!.get(i).display)
                userjsonObj.put("mobile", manattendlist?.datalist!!.get(i).mobile)
                userjsonObj.put("email", manattendlist?.datalist!!.get(i).email)
                userjsonObj.put("group", "account")
                paritinarray.put(userjsonObj)
                i++
            }
        }

        if (deviceattendlist != null ) {
            val num = deviceattendlist?.datalist!!.size!!
            var i = 0
            while (i < num){
                var userjsonObj: JSONObject = JSONObject()
                userjsonObj.put("id", deviceattendlist?.datalist!!.get(i).id)
                userjsonObj.put("name", deviceattendlist?.datalist!!.get(i).display)
                userjsonObj.put("mobile", deviceattendlist?.datalist!!.get(i).mobile)
                userjsonObj.put("email", deviceattendlist?.datalist!!.get(i).email)
                userjsonObj.put("group", "device")
                paritinarray.put(userjsonObj)
                //namearray.put(displaylist?.get(i))
                //pararray.put(namelist?.get(i))
                i++
            }
        }
        jsonObj.put("participants", paritinarray)
        return jsonObj?.toString()
    }

    fun initView() {
        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        //var titlebar_title: TextView = this.findViewById<TextView>(R.id.title)
        var btn_create: Button = this.findViewById<Button>(R.id.bt_createroom)
        mDateDialog=DialogWheelYearMonthDay(this@CreateConferenceRoomActivity)
        mDateDialog?.sureView?.setOnClickListener {
            var time=mDateDialog?.selectorYear.toString()+"-"+mDateDialog?.selectorMonth+"-"+mDateDialog?.selectorDay+
                    " "+mDateDialog?.selectorHour+":"+mDateDialog?.selectorMinute
            textinfo_create_conference_starttime.text = time
            mStartTime=time
            mDateDialog?.dismiss()
        }


        titlebar_back.isClickable = true

        val nowformatter = SimpleDateFormat("MM-dd HH:mm")
        val nowcurDate = Date(System.currentTimeMillis())//获取当前时间


        val strNow = nowformatter.format(nowcurDate)
        mStartTime = strNow
        textinfo_create_conference_starttime.text = mStartTime

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

        create_addman.setOnClickListener({ this.startChooseAttand()/*Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()*/ })
        create_adddevice.setOnClickListener({ this.startChooseDeviceAttand()/*Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()*/ })
        create_conference_starttime.setOnClickListener({
            mDateDialog?.show()
        })
        create_conference_costtime.setOnClickListener({ this.startCostTime()/*Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()*/ })

        /*listview= this.findViewById<ListView>(R.id.listCreateRoomView)
        listview?.setOnItemClickListener { adapterView, view, i, l ->
            listadapter!!.updateItemStr(false)
            when(i){
                0 -> this.startEditTitle()
                1 -> this.startChooseAttand()
                2 -> this.startChooseDeviceAttand()
                3 -> this.startDateTimer()
                4 -> this.startCostTime()

            }
        }*/

        mPresenter = CreateConferencePresenter(this)
        mCreateRoomPresenter = CreateConferenceRoomPresenter(this)
        mPresenter?.initListData()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val curDate = Date(System.currentTimeMillis())//获取当前时间

        mStartTime = formatter.format(curDate)
        userlistbean = ArrayList<ConferenceUserBean>()


    }

    override fun onConfirm(str: String?) {
        mStartTime = str
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        textinfo_create_conference_starttime.text = str

    }

    override fun onCancel() {

    }

    override fun onCostTimeConfirm(str: String?) {
        /*if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_COSTTIME, str)
            mCostTime = str
            listadapter!!.notifyDataSetChanged()
        }*/
        if (str == null || str.equals("")){
            mCostTime = "1"
        } else {
            mCostTime = str
        }
        textinfo_create_conference_costtime.text = str
    }

    override fun onCostTimeCancel() {

    }

    fun onAttandManUpdate(str: String?) {
        /*if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_ATTEND, str)
            listadapter!!.notifyDataSetChanged()
        }*/
        create_addman_num.text = str
    }

    fun onDeviceManUpdate(str: String?) {
        /*if (listadapter != null) {
            listadapter!!.updateItemStr(ITEM_DEVICE, str)
            listadapter!!.notifyDataSetChanged()
        }*/
        create_device_num.text = str
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != 0) {
            return
        }
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE_ATTEND) {
                manattendlist = data?.getSerializableExtra(KEY_ATTANDLIST) as AttendeeListBean
                onAttandManUpdate(manattendlist?.datalist!!.size.toString())
            } else if (requestCode == REQUEST_CODE_CHOOSE_DEVICE){
                deviceattendlist = data?.getSerializableExtra(KEY_ATTANDDEVICELIST) as AttendeeListBean
                onDeviceManUpdate(deviceattendlist?.datalist!!.size.toString())
            }
        }

    }
}