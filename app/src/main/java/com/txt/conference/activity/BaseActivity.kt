package com.txt.conference.activity

import android.app.Activity
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by jane on 2017/10/9.
 */
open class BaseActivity : Activity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}