package com.txt.conference.data

import android.content.Context
import com.common.data.SharedPreferencesFactory

/**
 * Created by jane on 2017/10/12.
 */
class TxSharedPreferencesFactory(context: Context?) : SharedPreferencesFactory(context, FILE_NAME) {
    companion object {
        val FILE_NAME = "tx_config"
    }

    private val KEY_ACCOUNT = "account"
    private val KEY_PASSWORD = "password"
    private val KEY_TOKEN = "token"

    fun setAccount(account: String?): Boolean {
        return setValue(KEY_ACCOUNT, account)
    }

    fun getAccount(): String? {
        return getString(KEY_ACCOUNT)
    }

    fun setPassword(password: String?): Boolean {
        return setValue(KEY_PASSWORD, password)
    }

    fun getPassword(): String? {
        return getString(KEY_PASSWORD)
    }

    fun setToken(token: String?): Boolean {
        return setValue(KEY_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(KEY_TOKEN)
    }

}