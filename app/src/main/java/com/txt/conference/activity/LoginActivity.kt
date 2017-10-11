package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.txt.conference.R
import com.txt.conference.presenter.LoginPresenter
import com.txt.conference.view.ILoginView
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by jane on 2017/10/9.
 */
class LoginActivity : ILoginView, BaseActivity(), View.OnClickListener {

    override fun jumpActivity() {
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    override fun back() {

    }

    var mLoginPresenter: LoginPresenter? = null

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.login_bt_login -> {
                mLoginPresenter?.doLogin(getAccount(), getPassword())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginPresenter = LoginPresenter(this)
        login_bt_login.setOnClickListener(this)
    }

    override fun getAccount(): String {
        return login_et_account.text.toString()
    }

    override fun getPassword(): String {
        return login_et_password.text.toString()
    }

    override fun showError(error: String) {

    }

    override fun hideError() {

    }

}