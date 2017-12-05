package com.txt.conference.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.CreateRoomListAdapter
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.CreateRoomListAdapterBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.CreateConferencePresenter
import com.txt.conference.presenter.CreateConferenceRoomPresenter
import com.txt.conference.utils.Constants
import com.txt.conference.utils.CostTimePickDialogUtil
import com.txt.conference.utils.DateTimePickDialogUtil
import com.txt.conference.utils.StatusBarUtil
import com.txt.conference.view.ICreateConferenceRoomView
import com.txt.conference.view.ICreateConferenceView
import kotlinx.android.synthetic.main.activity_createconferenceroom.*
import kotlinx.android.synthetic.main.activity_faceloginsettings.*
import kotlinx.android.synthetic.main.title_bar.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by pc on 2017/11/29.
 */


class FaceLoginSettingsActivity : BaseActivity(), View.OnClickListener  {


    val TAG = FaceLoginSettingsActivity::class.java.simpleName

    override fun jumpActivity() {
        onBackPressed()
    }

    override fun back() {

    }


    fun onFinished(){
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }

    fun onStartFaceAuth(){
        var i = Intent(this, FaceCollectActivity::class.java)
        startActivity(i)
    }

    fun getIsFacelogin(): Boolean? {
        return TxSharedPreferencesFactory(TxApplication.mInstance).getFaceLogin()
    }

    fun setFaceLogin(faceType: Boolean?) {
        TxSharedPreferencesFactory(TxApplication.mInstance).setFaceLogin(faceType)
    }

    fun getIsFaceAuthed(): Boolean? {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faceloginsettings)

        initView()
    }

    fun initView(){
        right_text.visibility = View.VISIBLE
        left_text.setText(R.string.cancel)
        right_text.setText(R.string.finish)
        left_text.setOnClickListener(this)
        right_text.setOnClickListener(this)
        item_facesetting_face_auth_layout.setOnClickListener(this)
        var isFaceLogin = getIsFacelogin()
        switch_facelogin.isChecked = isFaceLogin!!

        switch_facelogin.setOnCheckedChangeListener { button, checked ->
            if(checked){
                ULog.i(TAG, "checked" + checked)
            } else {
                ULog.i(TAG, "checked" + checked)
            }
            setFaceLogin(checked)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.left_text -> {
                onFinished()
            }
            R.id.item_facesetting_face_auth_layout-> {
                onStartFaceAuth()
            }
            R.id.right_text-> {
                onFinished()
            }
        }
    }
}