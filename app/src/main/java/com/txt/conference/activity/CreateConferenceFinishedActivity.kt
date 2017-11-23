package com.txt.conference.activity


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.bean.RoomBean

import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.Urls
import com.txt.conference.presenter.JoinRoomPresenter
import com.txt.conference.utils.CommonUtils
import com.txt.conference.view.IJoinRoomView
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by pc on 2017/11/7.
 */


class CreateConferenceFinishedActivity : BaseActivity(), IJoinRoomView {

    val TAG = CreateConferenceFinishedActivity::class.java.simpleName
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    companion object {
        var KEY_ROOM = "finished_room"
    }

    override fun jumpActivity() {

    }

    override fun back() {

    }

    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    override fun jumpToRoom(room: RoomBean, connect_token: String) {
        var i = Intent(this, RoomActivity::class.java)
        i.putExtra(RoomActivity.KEY_ROOM, room)
        i.putExtra(RoomActivity.KEY_CONNECT_TOKEN, connect_token)
        startActivity(i)
    }

    override fun showError(errorRes: Int) {
        Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
    }

    var room: RoomBean? = null
    var joinRoomPresenter: JoinRoomPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createconferencefinished)

        room = intent.getSerializableExtra(KEY_ROOM) as RoomBean
        if (room == null) {
            this.finish()
            return
        }
        joinRoomPresenter = JoinRoomPresenter(this)

        initView()
    }

    fun enterConference(){
        if (room != null) {
            joinRoomPresenter?.joinRoom(room!!, getToken())
            this.finish()
        }
    }

    private fun OpenPhoneAddress(){

        ULog.i(TAG, "OpenPhoneAddress" )

    }

    fun startPhoneAddress(){
        ULog.i(TAG, "startPhoneAddress")
        OpenPhoneAddress()
    }

    /*private fun startSendSms(){
        var date= DateUtils()
        ULog.i(TAG, "startSendSms" )
        var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.creator?.display,
                date.format(room?.start,DateUtils.HH_mm), room?.roomNo, Urls.HOST)
        intent.putExtra("sms_body", str_sms_Message)
        startActivity(intent)
    }*/


    fun startWeixinAddress(){
        ULog.i(TAG, "startWeixinAddress")
    }

    fun initView() {

        var time =System.currentTimeMillis()

        var titlebar_back: TextView = this.findViewById<TextView>(R.id.left_text)
        titlebar_back.setClickable(true)
        titlebar_back.setOnClickListener({ this.onBackPressed() })

        var btn_back: Button = this.findViewById<Button>(R.id.bt_back)
        btn_back.setOnClickListener { this.onBackPressed() }

        var btn_enter: Button = this.findViewById<Button>(R.id.bt_enter)
        btn_enter.setOnClickListener { this.enterConference() }

        var bt_sms: Button = this.findViewById<Button>(R.id.bt_sms_invite)
        bt_sms.setOnClickListener {
            //this.startSendSms()
            CommonUtils.startSendSms(this, room!!)
        }

        /*var btn_weixin: ImageView = this.findViewById<ImageView>(R.id.weixin_icon_id)
        btn_weixin.setOnClickListener { this.startWeixinAddress() }

        var btn_phone: ImageView = this.findViewById<ImageView>(R.id.phone_address_id)
        btn_phone.setOnClickListener { this.startPhoneAddress() }*/

        if (time >= room!!.start){
            btn_enter.visibility = View.VISIBLE
        } else {
            btn_enter.visibility = View.INVISIBLE
        }
    }

}