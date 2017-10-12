package com.txt.conference.activity

import android.content.Context
import android.util.Log

import com.common.data.SharedPreferencesFactory

/**
 * Created by jane on 2017/10/12.
 */

class TestActivity : SharedPreferencesFactory {

    private val KEY_SERVICEID = "serviceid"
    private val KEY_CHANNELNR = "channelNr"
    private val KEY_SHOW_GUID = "show_guid"

    protected constructor(curContext: Context, key: String) : super(curContext, key) {}

    constructor(context: Context) : super(context, FILE_NAME) {}

    val serviceId: String
        get() = getString(KEY_SERVICEID, null)

    fun setServiceId(serviceId: String): Boolean {
        return setValue(serviceId, 0)
    }

    val channelNr: Int
        get() = getInt(KEY_CHANNELNR)

    fun setChannelNr(channelNr: Int): Boolean {
        return setValue(KEY_CHANNELNR, channelNr)
    }

    val showGuid: Boolean
        get() = getBoolean(KEY_SHOW_GUID, true)

    fun setShowGuid(showGuid: Boolean): Boolean {
        return setValue(KEY_SHOW_GUID, showGuid)
    }

    companion object {
        private val FILE_NAME = "pivos_config"
    }
}
