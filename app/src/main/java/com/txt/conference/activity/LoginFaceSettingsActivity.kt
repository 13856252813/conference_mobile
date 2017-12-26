package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.txt.conference.R
import com.txt.conference.bean.LoginBean
import com.txt.conference.presenter.LoginPresenter
import com.txt.conference.view.ILoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.title_bar.*


/**
 * Created by pc on 2017/11/29.
 */


class LoginFaceSettingsActivity : ILoginView, BaseActivity(), View.OnClickListener  {

    val TAG = FaceLoginSettingsActivity::class.java.simpleName

    override fun jumpActivity() {
        onBackPressed()
    }

    override fun back() {

    }

    fun onFinished(){
        onBackPressed()
    }

    var mLoginPresenter: LoginPresenter? = null

    override fun jumpActivity(loginBean: LoginBean) {
        var i = Intent(this, FaceLoginSettingsActivity::class.java)
        i.putExtra(MainActivity.KEY_USER, loginBean)
        startActivity(i)
        finish()
    }

    override fun getAccount(): String {
        return login_et_account.text.toString()
    }

    override fun getPassword(): String {
        return login_et_password.text.toString()
    }

    override fun setAccount(account: String) {
        login_et_account.setText(account)
    }

    override fun setPassword(password: String) {
        login_et_password.setText(password)
    }

    override fun showError(error: String) {
        login_tv_error.visibility = View.VISIBLE
    }

    override fun hideError() {
        login_tv_error.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginfacesettings)

        mLoginPresenter = LoginPresenter(this)

        initView()
    }

    fun initView(){
        title_bar_title.setText(R.string.login_facesetting_firstlogin)
        left_text.setOnClickListener(this)
        login_bt_login.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.left_text -> {
                onBackPressed()
            }
            R.id.login_bt_login-> {
                mLoginPresenter?.doLogin(getAccount(), getPassword())
            }
        }
    }
}