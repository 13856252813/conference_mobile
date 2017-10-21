package com.txt.conference.activity

import android.app.Activity
import com.common.widget.LoadingView
import com.txt.conference.R
import com.txt.conference.view.IBaseView
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by jane on 2017/10/9.
 */
abstract class BaseActivity : Activity(), IBaseView, EasyPermissions.PermissionCallbacks {
    private var mLoadingView: LoadingView? = null

    override fun showLoading(msgRes: Int) {
        var msg = if (msgRes == 0) getString(R.string.loading) else getString(msgRes)
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