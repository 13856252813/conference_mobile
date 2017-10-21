package com.txt.conference.activity

import android.app.Activity
import android.widget.Toast
import com.common.widget.LoadingView
import com.txt.conference.R
import com.txt.conference.view.IBaseView
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by jane on 2017/10/9.
 */
abstract class BaseActivity : Activity(), IBaseView, EasyPermissions.PermissionCallbacks {
    private var mLoadingView: LoadingView? = null

    override fun showToast(msgRes: Int) {
        Toast.makeText(applicationContext, getString(msgRes), Toast.LENGTH_SHORT).show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(msgRes: Int) {
        var msg = if (msgRes == 0) getString(R.string.loading) else if (msgRes == 1) getString(R.string.createing_room) else getString(msgRes)
        if (mLoadingView == null) {
            mLoadingView = LoadingView(this, msg)
        } else {
            mLoadingView?.setMessage(msg)
        }
        mLoadingView?.show()
    }

    override fun hideLoading() {
        mLoadingView?.dismiss()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}