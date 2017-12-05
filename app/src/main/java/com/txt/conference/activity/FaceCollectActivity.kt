package com.txt.conference.activity

import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import com.txt.conference.R
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.view.SurfaceView
import android.view.WindowManager
import com.txt.conference.data.TxSharedPreferencesFactory
import com.reconova.faceid.utils.SFHCameraCallback
import com.reconova.processor.ImageHolder
import com.reconova.faceid.utils.ImageHelper
import com.reconova.faceid.utils.FileUtil
import com.reconova.faceid.utils.CheckFaceUtil
import com.common.utlis.ULog
import com.reconova.processor.RecoAliveProcessor
import android.widget.Toast
import com.reconova.processor.AliveParamConsts
import com.reconova.utils.FileTool
import android.os.AsyncTask
import android.os.SystemClock
import com.reconova.data.DataWrapper
import com.reconova.faceid.processor.ProcessorManager
import com.txt.conference.presenter.FaceAuthPresenter
import com.txt.conference.presenter.FaceLoginPresenter
import com.txt.conference.view.IFaceAuthView
import kotlinx.android.synthetic.main.activity_facecollect.*


/**
 * Created by pc on 2017/11/28.
 */
class FaceCollectActivity : BaseActivity(), IFaceAuthView, View.OnClickListener, Camera.PreviewCallback {
    override fun checkFinished() {
        finish()
    }

    override fun showError(error: String) {

    }

    override fun hideError() {

    }


    override fun jumpActivity() {

    }

    override fun back() {

    }

    fun initStartAnimation(){
        face_auto_circle_small.startAnimation(animation_clockwise)
        face_auto_circle_big.startAnimation(animation_anticlockwise)
    }
    fun stopAnimation(){
        face_auto_circle_small.clearAnimation()
        face_auto_circle_big.clearAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimation()
    }

    val TAG = "FaceCollectActivity"

    var animation_clockwise: Animation? = null
    var animation_anticlockwise: Animation? = null

    var mCameraSurfaceView: SurfaceView? = null    // 用来显示摄像头视频。
    var mCameraDrawFrameLayout: FrameLayout? = null // 上述两个View的父布局。
    val mPreference: TxSharedPreferencesFactory? = null

    var mCameraControlCallback: SFHCameraCallback? = null    // 用来控制摄像头的 打开、预览、关闭及视频帧的回调。
    var mImageHolder: ImageHolder? = null    // 视频帧的数据。

    var mProcessorManager: ProcessorManager?  = null	// 用来控制人脸活体检测处理器的运行。
	var mFaceAliveProcessor: ProcessorManager.IProcessor? = null	// 人脸活体检测处理器

    var takedPicture = false

    val INIT_DETECT_TIME: Long = -1
    val MAX_DETECT_MS: Long = 8000
    var mFirstHasDetectTime = INIT_DETECT_TIME    // 一次活体验证中，第一次检测到人脸的时间。

    var mAliveType = RecoAliveProcessor.ALIVE_TYPE_MOUTH_OPEN_CLOSE

    val HEADS = 0
    val EYES = 1
    val MOUTHS = 2
    val CHECKFINISHED = 3
    var currentCheckType = 0

    var mAliveProcessor: RecoAliveProcessor? = null
    var mInitNativeLibTask: InitNativeLibTask? = null

    var mFaceAuthPresenter: FaceAuthPresenter? = null

    var runnable: Runnable = object : Runnable {
        override fun run() {

            if (startCheck()) {

            }
            //handler.postDelayed(this, 3000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facecollect)

        this.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN)
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        face_bt_retry.setOnClickListener(this)
        left_backup.setOnClickListener(this)
        animation_clockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_clockwise)
        animation_anticlockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_anticlockwise)

        findViews()
        initStartAnimation()
        initLayout()
        initNative()
        //var handler = Handler()
        //handler.postDelayed(runnable, 3000)
        state_textview_small.setText(R.string.account_face_box)
    }

    fun noNeedToProcessImage(): Boolean {
        if (this.isFinishing) return true // Activity已经销毁
        return false
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        camera?.addCallbackBuffer(data)
        if (noNeedToProcessImage()) {
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
        mProcessorManager?.startProcessor(mFaceAliveProcessor)
    }

    private fun initNative(){
		ULog.i(TAG, "initNative");
		mInitNativeLibTask = InitNativeLibTask()
		mInitNativeLibTask!!.execute()
		currentCheckType = HEADS
	}

    private fun initLayout() {
        mCameraControlCallback = SFHCameraCallback(mCameraSurfaceView?.getHolder(),
                this, this)
        mCameraControlCallback?.setAdjustView(mCameraDrawFrameLayout)
        mCameraControlCallback?.setAdjustType(SFHCameraCallback.ADJUST_VIEW_HEIGHT)

    }

    fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    fun AuthCheck() {
        if (mFaceAuthPresenter == null){
            mFaceAuthPresenter = FaceAuthPresenter(this)
        }
        mFaceAuthPresenter?.doAuthCheck(getToken()!!, FileUtil.picFullPathFileName)
    }

    fun startCheck(): Boolean {
        ULog.i(TAG, "startCheck:" + takedPicture)
        if (takedPicture == true) {
            return false
        }
        mCameraControlCallback?.doTakePicture()
        if (CheckFaceUtil.getInstance().isFaceFile(FileUtil.picFullPathFileName)) {
            takedPicture = true
            try {
                Thread.sleep(2000)
                AuthCheck()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

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

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.face_bt_retry -> {
                //onBackPressed()
            }
            R.id.left_backup-> {
                onBackPressed()
            }
        }
    }

    private fun StartCheckLive(type: Int): Boolean {

        ULog.i(TAG, "StartCheckLive")
        when (type) {
            HEADS -> {
                //mNoticeTextView.setText(getString(R.string.shake_head))
                runOnUiThread { state_textview_small.setText(R.string.account_face_left_right) }
                mAliveType = RecoAliveProcessor.ALIVE_TYPE_HEAD_SHAKING
            }
            EYES -> {
                runOnUiThread { state_textview_small.setText(R.string.account_face_eye) }
                //mNoticeTextView.setText(getString(R.string.bling_eye))
                mAliveType = RecoAliveProcessor.ALIVE_TYPE_EYE_BLINK
            }
        }/*case MOUTHS:
				mNoticeTextView.setText(getString(R.string.open_mouch));
				mAliveType = RecoAliveProcessor.ALIVE_TYPE_MOUTH_OPEN_CLOSE;
				break;*/
        mAliveProcessor?.reset()
        mFirstHasDetectTime = INIT_DETECT_TIME
        return false
    }

    fun initProcessor() {

        ULog.i(TAG, "initProcessor");
        mProcessorManager = ProcessorManager()
        mFaceAliveProcessor = FaceAliveDetectProcessor()
        mImageHolder = ImageHolder(null, SFHCameraCallback.sPreviewWidth,
        SFHCameraCallback.sPreViewHeight, ImageHolder.YUVSP420_TYPE);
        //mDrawRectUtil =  DrawFaceRectUtil()

        mAliveProcessor = RecoAliveProcessor.getInstance()

        StartCheckLive(currentCheckType);

    }

    fun doTakePicture(picNo: Int) {

        mCameraControlCallback?.doTakePicture(picNo)

    }

    fun AliveCheckOK(picNo: Int) {
        doTakePicture(picNo)

    }

    public inner class FaceAliveDetectProcessor : ProcessorManager.IProcessor {
        override fun onProcess(): Any {
            ULog.i(TAG, "onProcess")
			var faceRects: List<DataWrapper.MFaceRect>  = mAliveProcessor!!.aliveDetect(
					mImageHolder!!, mAliveType, SystemClock.elapsedRealtime())
			// 从第一次检测到人脸开始计算超时
			if (mFirstHasDetectTime == INIT_DETECT_TIME) {
				mFirstHasDetectTime = System.currentTimeMillis();
			}

			return faceRects
        }

        override fun onPostExcute(result: Any?) {
            ULog.i(TAG, "onPostExcute")
			if (this@FaceCollectActivity.isFinishing()) {
				return
			}
			if (mFirstHasDetectTime == INIT_DETECT_TIME) return;



			var isTimeOut: Boolean = (System.currentTimeMillis() - mFirstHasDetectTime) > MAX_DETECT_MS
			/*drawFaceRects((List<ImageStruct.ImageResult.FaceRect>) result, mImageHolder.getWidth(),
					mImageHolder.getHeight());*/
			// 活体成功后或者超时，就显示活体检测结果。
			if (mAliveProcessor!!.isAlive() || isTimeOut) {

				//mHasPressedButton = false;

				if (mAliveProcessor!!.isAlive()) {
					//mStateTextView.setTextColor(Color.GREEN);
					//CustomToast.showLong(getApplicationContext(), "活体检测成功！");
					ULog.i(TAG, "活体验证成功:" + currentCheckType);
					when(currentCheckType){
                        HEADS -> {
                            currentCheckType++
                            StartCheckLive(currentCheckType)
                        }
                        EYES -> {
                            currentCheckType++
                            runOnUiThread { state_textview_small.setText(R.string.account_face_confirming) }
                            AliveCheckOK(currentCheckType)
                        }
					}
				} else {
					ULog.i(TAG, "活体验证失败")
				}
			}
        }


    }

    public inner class InitNativeLibTask : AsyncTask<Any, Any, Boolean>() {

        val STATE_NOT_START = 0
        val STATE_RUNNING = 1
        val STATE_DONE = 2
        val STATE_CANCEL = 3

        var mState = STATE_NOT_START

        public override fun onPreExecute() {
            //mRefreshButton.setVisibility(View.GONE);
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        override fun doInBackground(vararg arg0: Any): Boolean? {
            mState = STATE_RUNNING
            /** 拷贝模型文件到该私有路径下   */
            val fileDir = applicationContext.filesDir
                    .absolutePath
            FileTool.copyAssetFiles(applicationContext, fileDir)
            val init = RecoAliveProcessor.getInstance().init(applicationContext, fileDir)
            if (init) {
                // 设置摇头防抖阈值。
                RecoAliveProcessor.getInstance().setAliveParam(AliveParamConsts.HEAD_DEFENCE_THRESHOLD, 3.0f)
                // 设置摇头通过灵敏度。
                RecoAliveProcessor.getInstance().setAliveParam(AliveParamConsts.HEAD_PASSING_THRESHOLD, 3.0f)
                // 设置眨眼通过灵敏度。
                RecoAliveProcessor.getInstance().setAliveParam(AliveParamConsts.EYE_PASSING_THRESHOLD, 3.0f)
                // 设置张嘴通过灵敏度。
                RecoAliveProcessor.getInstance().setAliveParam(AliveParamConsts.MOUTH_PASSING_THRESHOLD, 3.0f)
                RecoAliveProcessor.getInstance().faceConfig(0.6f, 80, 512)
            }
            return init
        }

        override fun onPostExecute(initOk: Boolean?) {
            mState = STATE_DONE
            if (initOk!!) {

                ULog.i(TAG, "initOk")
                initProcessor()
            } else {
                RecoAliveProcessor.getInstance().finalize()
                Toast.makeText(applicationContext, "初始化失败,请检查网络或重试",
                        Toast.LENGTH_LONG).show()
            }

        }

        override fun onCancelled() {
            mState = STATE_CANCEL
        }

    }
}