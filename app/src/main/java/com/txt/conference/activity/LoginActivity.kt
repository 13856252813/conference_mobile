package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.LoginBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.LoginPresenter
import com.txt.conference.view.ILoginView
import com.txt.conference.widget.CustomDialog
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by jane on 2017/10/9.
 */
class LoginActivity : ILoginView, BaseActivity(), View.OnClickListener {
    override fun setAccount(account: String) {
        login_et_account.setText(account)
    }

    override fun setPassword(password: String) {
        login_et_password.setText(password)
    }

    override fun jumpActivity() {

    }

    override fun jumpActivity(loginBean: LoginBean) {
        var i = Intent(this, MainActivity::class.java)
        i.putExtra(MainActivity.KEY_USER, loginBean)
        startActivity(i)
        this.finish()
    }

    override fun back() {

    }

    var mLoginPresenter: LoginPresenter? = null

    var isFaceLogin: Boolean? = false
    fun jumpOneKeyEnter() {
        var i = Intent(this, OneKeyEnterActivity::class.java)
        startActivity(i)
    }

    fun jumpFaceLogin() {

        if (isFaceLogin == true) {
            var i = Intent(this, FaceLoginActivity::class.java)
            startActivity(i)
        } else {
            var i = Intent(this, LoginFaceSettingsActivity::class.java)
            startActivity(i)
        }
    }

    private fun showCommonDialog(){

        if (isFaceLogin == true) {
            var i = Intent(this, FaceLoginActivity::class.java)
            startActivity(i)
        } else {
            CustomDialog.showCommonDialog(this,
                    resources.getString(R.string.facelogin_dialog_title),
                    resources.getString(R.string.facelogin_dialog_message),
                    resources.getString(R.string.facelogin_dialog_confirm),
                    resources.getString(R.string.facelogin_dialog_cancel),
                    object : com.txt.conference.widget.CustomDialog.DialogClickListener {
                        override fun confirm() {
                            jumpFaceLogin()
                        }

                        override fun cancel() {

                        }

                    }
            )
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.login_bt_login -> {
                mLoginPresenter?.doLogin(getAccount(), getPassword())
            }
            R.id.login_bt_onekeyenter-> {
                jumpOneKeyEnter()
            }
            R.id.face_bt_login-> {
                //jumpFaceLogin()
                showCommonDialog()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginPresenter = LoginPresenter(this)
        login_bt_login.setOnClickListener(this)
        login_bt_onekeyenter.setOnClickListener(this)
        face_bt_login.setOnClickListener(this)

        isFaceLogin = TxSharedPreferencesFactory(TxApplication.mInstance).getFaceLogin()
    }

    override fun getAccount(): String {
        return login_et_account.text.toString()
    }

    override fun getPassword(): String {
        return login_et_password.text.toString()
    }

    override fun showError(error: String) {
        login_tv_error.visibility = View.VISIBLE
    }

    override fun hideError() {
        login_tv_error.visibility = View.INVISIBLE
    }

}