package com.txt.conference.view

/**
 * Created by jane on 2017/10/11.
 */
interface IBaseView {
    fun jumpActivity()
    fun back()
    fun showToast(msgRes: Int)
    fun showToast(msg: String)
    fun showLoading(msgRes: Int)
    fun hideLoading()
}