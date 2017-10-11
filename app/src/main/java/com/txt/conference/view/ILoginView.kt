package com.txt.conference.view

/**
 * Created by jane on 2017/10/7.
 */
interface ILoginView : IBaseView {
    fun getAccount(): String
    fun getPassword(): String

    fun showError(error: String)
    fun hideError()
}