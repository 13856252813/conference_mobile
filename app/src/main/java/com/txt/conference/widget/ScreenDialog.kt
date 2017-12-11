package com.tofu.conference.widget

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import com.intel.webrtc.base.RemoteStream
import com.tofu.conference.utils.DensityUtils
import com.txt.conference.R


/**
 * Created by pc on 2017/10/27.
 */

class ScreenDialog private constructor(private val mContext: Activity, themeResId: Int) : Dialog(mContext, themeResId),
        View.OnClickListener{

    private var mScreenHeight: Int = 0
    private var mScreenWidth: Int = 0
    private val mImageScale: ImageView
    private var mImageHide: ImageView
    val screenContainer: LinearLayout
    private var view: View? = null
    private var mScreenViewListener: ScreenViewListener? = null
    var remoteScreenStream: RemoteStream? = null
    private var textArray: Array<ImageView>? = null


    init {
        view = LayoutInflater.from(mContext).inflate(R.layout.share_screen, null, false)
        screenContainer = view?.findViewById(R.id.stream_layout)!!
        mImageScale = view?.findViewById(R.id.finish_screen)!!
        mImageHide = view?.findViewById(R.id.hide_screen)!!

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        mScreenHeight = DensityUtils.getHeight(mContext)
        mScreenWidth = DensityUtils.getWidth(mContext)
        setWindowScale(mScreenWidth, (mScreenHeight))
        mImageScale!!.setOnClickListener(this)
        mImageHide!!.setOnClickListener(this)
        textArray = arrayOf(mImageScale, mImageHide)

    }


    private fun setWindowScale(width: Int, height: Int) {
        val lp = window!!.attributes
        lp.width = width
        lp.height = height
        window!!.attributes = lp
        window!!.setDimAmount(0.2f)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.finish_screen -> {
                if (mScreenViewListener != null) {
                    mScreenViewListener!!.disAbleView(remoteScreenStream)
                }
                dismiss()
            }
            R.id.hide_screen -> {
                val width = window!!.attributes.width
                if (width < mScreenWidth) {
                    setWindowScale(mScreenWidth, mScreenHeight)
                } else {
                    setWindowScale((mScreenWidth * 0.8).toInt(), (mScreenHeight * 0.8).toInt())
                }
            }
        }
    }


    fun setScreenViewListener(listener: ScreenViewListener) {
        mScreenViewListener = listener
    }

    interface ScreenViewListener {
        fun disAbleView(remoteStream: RemoteStream?)
    }

    companion object {
        var mInstance: ScreenDialog? = null
        fun getScreenDialog(context: Activity): ScreenDialog {
            if (mInstance == null) {
                mInstance = ScreenDialog(context, R.style.DialogStyle)
            }
            return mInstance as ScreenDialog
        }
    }

}
