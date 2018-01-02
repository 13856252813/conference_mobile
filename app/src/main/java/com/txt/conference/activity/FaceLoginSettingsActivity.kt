package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.data.TxSharedPreferencesFactory
import kotlinx.android.synthetic.main.activity_faceloginsettings.*
import kotlinx.android.synthetic.main.title_bar.*


/**
 * Created by pc on 2017/11/29.
 */


class FaceLoginSettingsActivity : BaseActivity(), View.OnClickListener  {


    private val COLLECTED = 1

    val TAG = FaceLoginSettingsActivity::class.java.simpleName!!
    override fun jumpActivity() {
        onBackPressed()
    }

    override fun back() {

    }

    private fun showSettingToast(strToask: String?){
        Toast.makeText(applicationContext, strToask, Toast.LENGTH_SHORT).show()
    }

    private fun onFinished(){
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }

    private fun onStartFaceAuth(){
        var i = Intent(this, FaceCollectActivity::class.java)
        startActivity(i)
    }

    private fun getIsFacelogin(): Boolean? {
        return TxSharedPreferencesFactory(TxApplication.mInstance).getFaceLogin()
    }

    fun setFaceLogin(faceType: Boolean?) {
        TxSharedPreferencesFactory(TxApplication.mInstance).setFaceLogin(faceType)
    }

    fun getIsFaceAuthed(): Boolean? {
        return false
    }

    private fun getIsFaceCollect(): Int?{
        return  TxSharedPreferencesFactory(TxApplication.mInstance).getIsCollect()
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

        switch_facelogin.setOnCheckedChangeListener { _, checked ->
            if(checked){
                ULog.i(TAG, "checked" + checked)
                if (getIsFaceCollect() == COLLECTED  ){
                } else {
                    switch_facelogin.isChecked=false
                    showSettingToast(getString(R.string.facelogin_should_face_collect_first))
                }
            } else {
                ULog.i(TAG, "checked" + checked)
            }
            setFaceLogin(checked)
        }
    }

    override fun onResume() {
        super.onResume()
        if (getIsFaceCollect() == COLLECTED  ){ // collected
            face_collect_type.text = getString(R.string.facesetting_already_confirm_text)
        } else {
            face_collect_type.text = getString(R.string.facesetting_not_confirm_text)
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