package com.txt.conference.view


/**
 * Created by pc on 2017/12/1.
 */
interface IFaceAuthView : IBaseView {
    fun checkOK()
    fun checkFailed()

    fun showError(error: String)
    fun hideError()
}