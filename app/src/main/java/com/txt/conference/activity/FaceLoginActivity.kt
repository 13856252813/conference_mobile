package com.txt.conference.activity

import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import com.txt.conference.R
import kotlinx.android.synthetic.main.activity_facelogin.*
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.view.SurfaceView
import android.view.WindowManager
import com.txt.conference.data.TxSharedPreferencesFactory
import com.reconova.processor.ImageHolder
import android.os.Handler
import android.widget.Toast
import com.common.utlis.ULog
import com.reconova.faceid.utils.*
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.LoginBean
import com.txt.conference.presenter.FaceLoginPresenter
import com.txt.conference.view.IFaceLoginView
import com.reconova.faceid.utils.UploadUtil
import com.reconova.faceid.utils.FileUtil
import java.io.File


/**
 * Created by pc on 2017/11/28.
 */
class FaceLoginActivity : BaseActivity(), IFaceLoginView, View.OnClickListener, Camera.PreviewCallback {

    override fun jumpActivity(loginBean: LoginBean) {
        runOnUiThread {
            var i = Intent(this, MainActivity::class.java)
            i.putExtra(MainActivity.KEY_USER, loginBean)
            startActivity(i)
            this.finish()
        }
    }

    override fun getAccount(): String {
        return TxSharedPreferencesFactory(TxApplication.mInstance!!).getAccount()!!
    }

    override fun setAccount(account: String) {
        TxSharedPreferencesFactory(TxApplication.mInstance!!).setAccount(account)
    }

    override fun showError(error: String) {
        runOnUiThread {
            state_textview_big.visibility = View.VISIBLE
            state_textview_big.text = getString(R.string.facelogin_error)
        }
    }

    override fun hideError() {
        runOnUiThread {
            state_textview_big.visibility = View.INVISIBLE
        }
    }


    override fun jumpActivity() {

    }

    override fun back() {

    }

    private fun deleteLastPhoto(){
        var file = File(FileUtil.picFullPathFileName)
        if (file.exists()){
            file.delete()
        }
    }

    override fun showToast(msgRes: Int) {
        super.showToast(msgRes)
        face_bt_retry.visibility = View.VISIBLE
        state_textview_big.text = getString(R.string.account_face_failed)
        state_textview_small.visibility = View.INVISIBLE

    }

    override fun showToast(msg: String) {
        super.showToast(msg)
        face_bt_retry.visibility = View.VISIBLE
        state_textview_big.text = getString(R.string.account_face_failed)
        state_textview_small.visibility = View.INVISIBLE
    }

    private fun initStartAnimation(){
        face_auto_circle_small.startAnimation(animation_clockwise)
        face_auto_circle_big.startAnimation(animation_anticlockwise)

    }
    private fun stopAnimation(){
        face_auto_circle_small.clearAnimation()
        face_auto_circle_big.clearAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimation()
    }

    private val TAG = "FaceLoginActivity"

    var animation_clockwise: Animation? = null
    var animation_anticlockwise: Animation? = null

    var mCameraSurfaceView: SurfaceView? = null    // 用来显示摄像头视频。
    var mCameraDrawFrameLayout: FrameLayout? = null // 上述两个View的父布局。
    val mPreference: TxSharedPreferencesFactory? = null

    var mCameraControlCallback: SFHCameraCallback? = null    // 用来控制摄像头的 打开、预览、关闭及视频帧的回调。
    val mImageHolder: ImageHolder? = null    // 视频帧的数据。
    var takedPicture = false

    var mFaceLoginPresenter: FaceLoginPresenter? = null
    var handler: Handler? = null
    var runnable: Runnable = object : Runnable {
        override fun run() {

            if (startCheck()) {

            } else {

                handler!!.postDelayed(this, 3000)
            }
            //handler.postDelayed(this, 3000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facelogin)

        this.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN)
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        face_bt_backto_login.setOnClickListener(this)
        face_bt_onekeyenter.setOnClickListener(this)
        face_bt_retry.setOnClickListener(this)

        animation_clockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_clockwise)
        animation_anticlockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_anticlockwise)

        findViews()
        initStartAnimation()
        initLayout()

        deleteLastPhoto()
        handler = Handler()
        handler!!.postDelayed(runnable, 3000)

        //UploadCheckFile()
    }

    fun noNeedToProcessImage(): Boolean {
        if (this.isFinishing) return true // Activity已经销毁
        return false
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        camera?.addCallbackBuffer(data)
        if (noNeedToProcessImage()) {
            // 清空画布
            //drawFaceRects(null, 0, 0)
            return
        }
        /** 根据手机横竖屏状态与相机安装的方向获取出需要图片需要旋转的角度，并选择图片 */
        val rotateAngle = mCameraControlCallback?.getImageRotation()
        val rotateData = ImageHelper.rotateYUV420sp(data,
                SFHCameraCallback.sPreviewWidth,
                SFHCameraCallback.sPreViewHeight, rotateAngle!!)

        /** 如果图片旋转90或者270 ，交换传入数据的宽与高。 */
        if (rotateAngle % 180 == 0) {
            mImageHolder?.setSize(SFHCameraCallback.sPreviewWidth,
                    SFHCameraCallback.sPreViewHeight)
        } else {
            mImageHolder?.setSize(SFHCameraCallback.sPreViewHeight,
                    SFHCameraCallback.sPreviewWidth)
        }
        mImageHolder?.setImageData(rotateData)
        //mProcessorManager.startProcessor(mFaceAliveProcessor)
    }

    private fun initLayout() {
        mCameraControlCallback = SFHCameraCallback(mCameraSurfaceView?.getHolder(),
                this, this)
        mCameraControlCallback?.setAdjustView(mCameraDrawFrameLayout)
        mCameraControlCallback?.setAdjustType(SFHCameraCallback.ADJUST_VIEW_HEIGHT)



    }

    fun UploadCheckFile() {
        if (mFaceLoginPresenter == null) {
            mFaceLoginPresenter = FaceLoginPresenter(this,this)
        }
        mFaceLoginPresenter?.doFaceLogin(getAccount(), FileUtil.picFullPathFileName)

    }

    fun startCheck(): Boolean {
        ULog.i(TAG, "startCheck:" + takedPicture)
        if (takedPicture == true) {
            return false
        }
        mCameraControlCallback?.doTakePicture()

        if (CheckFaceUtil.getInstance().isFaceFile(FileUtil.picFullPathFileName)) {
            takedPicture = true
            UploadCheckFile()

        } else {
            takedPicture = false
            return false
        }
        return true

    }

    private fun findViews() {
        mCameraDrawFrameLayout = this
                .findViewById<View>(R.id.frame_camera_draw) as FrameLayout

        mCameraSurfaceView = this
                .findViewById<View>(R.id.surfaceview_camera) as SurfaceView
    }

    fun jumpOneKeyEnter() {
        var i = Intent(this, OneKeyEnterActivity::class.java)
        startActivity(i)
        finish()
    }

    fun jumpLogin() {
        var i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    fun retryLogin() {
        face_bt_retry.visibility = View.INVISIBLE
        state_textview_small.visibility = View.VISIBLE
        state_textview_big.text = getString(R.string.facesetting_login)
        state_textview_small.text = getString(R.string.account_face_box)
        takedPicture = false
        handler!!.postDelayed(runnable, 2000)
        deleteLastPhoto()
    }
    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.face_bt_backto_login -> {
                jumpLogin()
            }
            R.id.face_bt_onekeyenter -> {
                jumpOneKeyEnter()
            }
            R.id.face_bt_retry -> {
                retryLogin()
            }
        }
    }


}