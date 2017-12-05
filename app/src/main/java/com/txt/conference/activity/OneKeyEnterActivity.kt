package com.txt.conference.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.JoinRoomPresenter
import com.txt.conference.presenter.OneKeyEnterPresenter
import com.txt.conference.view.IJoinRoomView
import com.txt.conference.view.IOneKeyEnterView

import kotlinx.android.synthetic.main.activity_onekeyenter.*

/**
 * Created by pc on 2017/11/08.
 */
class OneKeyEnterActivity : BaseActivity(), IOneKeyEnterView, IJoinRoomView, View.OnClickListener {
    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    var mPreference: TxSharedPreferencesFactory? = null

    override fun jumpToRoom(room: RoomBean, connect_token: String) {
        var i = Intent(this, RoomActivity::class.java)
        i.putExtra(RoomActivity.KEY_ROOM, room)
        i.putExtra(RoomActivity.KEY_CONNECT_TOKEN, connect_token)
        startActivity(i)
    }

    fun saveIsLogin(type: String?) {
        if (mPreference == null) {
            mPreference = TxSharedPreferencesFactory(TxApplication.mInstance!!)
        }
        mPreference?.setLogin(type)
    }

    fun enterConference(roomBean: RoomBean){
        if (roomBean != null) {
            saveIsLogin("false")
            joinRoomPresenter?.joinRoom(roomBean!!, getToken())
            this.finish()
        }
    }

    override fun showError(errorRes: Int) {
        Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
    }

    override fun jumpActivity(roomBean: RoomBean) {
        ULog.i(TAG, "room id: " + roomBean.roomNo)
        enterConference(roomBean)
    }

    override fun getRoomNo(): String {
        return enter_et_roomno.text.toString()
    }

    override fun getUserName(): String {
        return enter_et_name.text.toString()
    }

    override fun setRoomNo(roomno: String) {

    }

    override fun setUserName(username: String) {

    }

    override fun showError(error: String) {

    }

    override fun hideError() {

    }

    override fun jumpActivity() {

    }

    override fun back() {

    }

    fun checkAndInitUri(): Boolean?{
        val intent = intent
        val scheme = intent.scheme
        val uri = intent.data
        if (scheme == null || uri == null){
            return false
        }
        if (scheme.equals("yzconference") == false){
            return false
        }
        mstrHost = uri.host
        mstrOpentype = uri.getQueryParameter("opentype")
        mstrRoomNo = uri.getQueryParameter("roomNo")
        return true
    }

    val TAG = OneKeyEnterActivity::class.java.simpleName
    var mOneKeyEnterPresenter: OneKeyEnterPresenter? = null
    var joinRoomPresenter: JoinRoomPresenter? = null
    var mstrHost: String? = null
    var mstrOpentype: String? = null
    var mstrRoomNo: String? = null


    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.enter_bt_enter -> {
                mOneKeyEnterPresenter?.doOneKeyEnter(getRoomNo(), getUserName())
            }
            R.id.enter_bt_back -> {
                this.onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onekeyenter)
        mOneKeyEnterPresenter = OneKeyEnterPresenter(this)
        joinRoomPresenter = JoinRoomPresenter(this)
        enter_bt_enter.setOnClickListener(this)
        enter_bt_back.setOnClickListener(this)

        if (checkAndInitUri() == true){
            enter_bt_back.visibility = View.INVISIBLE
            if (mstrRoomNo != null){
                enter_et_roomno.setText(mstrRoomNo)
            }
        }
    }
}