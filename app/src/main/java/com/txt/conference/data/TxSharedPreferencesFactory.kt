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
    private val KEY_USER_NAME = "username"//usr to display
    private val KEY_PHONE = "phone"
    private val KEY_ID = "id"
    private val KEY_LOGIN = "login_type"
    private val KEY_FACE_COLLECT = "face_collect"
    private val KEY_ONEKEY_ROOMNO = "onekey_roomno"
    private val KEY_ONEKEY_NAME = "onekey_username"
    private val KEY_FIRST = "first_run"
    private val KEY_FACELOGIN = "facelogin"

    fun setPhoneNumber(phone: String?): Boolean {
        return setValue(KEY_PHONE, phone)
    }

    fun getPhoneNumber(): String? {
        return getString(KEY_PHONE, "")
    }

    fun setId(id: String?): Boolean {
        return setValue(KEY_ID, id)
    }

    fun getId(): String? {
        return getString(KEY_ID, "")
    }

    fun setUserName(username: String?): Boolean {
        return setValue(KEY_USER_NAME, username)
    }

    fun getUserName(): String? {
        return getString(KEY_USER_NAME, "")
    }

    fun setAccount(account: String?): Boolean {
        return setValue(KEY_ACCOUNT, account)
    }

    fun getAccount(): String? {
        return getString(KEY_ACCOUNT, "")
    }

    fun setPassword(password: String?): Boolean {
        return setValue(KEY_PASSWORD, password)
    }

    fun getPassword(): String? {
        return getString(KEY_PASSWORD, "")
    }

    fun setToken(token: String?): Boolean {
        return setValue(KEY_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(KEY_TOKEN)
    }

    fun setOneKeyEnterRoomNo(token: String?): Boolean {
        return setValue(KEY_ONEKEY_ROOMNO, token)
    }

    fun getOneKeyEnterRoomNo(): String? {
        return getString(KEY_ONEKEY_ROOMNO)
    }

    fun setOneKeyName(token: String?): Boolean {
        return setValue(KEY_ONEKEY_NAME, token)
    }

    fun getOneKeyName(): String? {
        return getString(KEY_ONEKEY_NAME)
    }

    fun setLogin(token: String?): Boolean {
        return setValue(KEY_LOGIN, token)
    }

    fun getLogin(): String? {
        return getString(KEY_LOGIN)
    }

    fun setIsCollect(token: Int): Boolean {
        return setValue(KEY_FACE_COLLECT, token)
    }

    fun getIsCollect(): Int? {
        return getInt(KEY_FACE_COLLECT)
    }

    fun setFirstRun(firstType: Boolean?): Boolean {
        return setValue(KEY_FIRST, firstType!!)
    }

    fun getIsFirstRun(): Boolean? {
        return getBoolean(KEY_FIRST, true)
    }

    fun setFaceLogin(firstType: Boolean?): Boolean {
        return setValue(KEY_FACELOGIN, firstType!!)
    }

    fun getFaceLogin(): Boolean? {
        return getBoolean(KEY_FACELOGIN, false)
    }
}