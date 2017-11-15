package com.txt.conference.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.common.widget.LoadingView
import com.txt.conference.R
import com.txt.conference.view.IBaseView
import pub.devrel.easypermissions.EasyPermissions
import com.txt.conference.utils.StatusBarUtil


/**
 * Created by jane on 2017/10/9.
 */
abstract class BaseActivity : Activity(), IBaseView, EasyPermissions.PermissionCallbacks {
    private var mLoadingView: LoadingView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
    }

    override fun showToast(msgRes: Int) {
        Toast.makeText(applicationContext, getString(msgRes), Toast.LENGTH_SHORT).show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(msgRes: Int) {
        var msg = if (msgRes == 0) getString(R.string.loading) else getString(msgRes)
        if (mLoadingView == null) {
            mLoadingView = LoadingView(this, msg)
        } else {
            mLoadingView?.setMessage(msg)
        }
        if (!mLoadingView?.isShowing!!) {
            mLoadingView?.show()
        }
    }

    override fun hideLoading() {
        if (mLoadingView != null) {
            mLoadingView?.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        hideLoading()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right)
    }

    override fun onDestroy() {
        if(mLoadingView!=null && mLoadingView?.isShowing!!){
            mLoadingView?.dismiss()
            mLoadingView=null
        }
        super.onDestroy()
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


     open fun setStatusBar() {
         StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimary),0)
    }

}