package com.reconova.faceid.utils;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.common.utlis.ULog;

/**
 * SFHCameraCallback类根据指定的Surface的生命周期来控制摄像头的打开、预览 、关闭；
 * 同时，在预览的时候，将SurfaceHoler与被打开的摄像头绑定，使之能显示摄像头预览的的画面；
 * 最后还提供调整界面显示区 控件的长宽，来保证预览视频不会变形。
 */
public class SFHCameraCallback implements SurfaceHolder.Callback {
	public static final String TAG = "SFHCAMERACALLBACK";
	
	/**  根据 view的宽/长  与 预览 图片的 宽/长的 大小 关系 来决定调整 view的宽度 或者 高度 。 */
	public static final int ADJUST_VIEW_BY_RIATIO = 0; 
	/** 调整view的高度 ，使得view的 长宽 比 与 预览 图片的 一致 。 */
	public static final int ADJUST_VIEW_WIDTH = 1;
	/** 调整view的 宽度 ，使得view的 长宽 比 与 预览 图片的 一致 。 */
	public static final int ADJUST_VIEW_HEIGHT = 2;	 

	public static int sPreviewWidth = 640;//640;	// 预览图片宽度
	public static int sPreViewHeight = 480;//480; // 预览图片高度


	private SurfaceHolder mSurfaceHolder;	// 用于显示预览界面的surfaceholder
	private Camera.PreviewCallback mPreviewCallback; // 用于获取预览数据回调函数
	private Activity mActivity;
	
	private Camera mOpenCamera;	// 已打开的摄像头
	public int mOpenCameraId = -1; // 已打开的摄像头id
	public int mOrientation = 270;
	
	private View mAdjustView;	// 需要被调整长宽 比例的 view。通常为 视频 显示控件或者其 父控件 。
	private int mAdjustType = ADJUST_VIEW_BY_RIATIO;

	private boolean isPreviewing = false;
	public void setAdjustType(int adjustType) {
		mAdjustType = adjustType;
	}

	public int mTypeNo = 0;

	@SuppressWarnings("deprecation")
	public SFHCameraCallback(SurfaceHolder holder,
			Camera.PreviewCallback previewCallback, Activity activity) {
		
		this.mSurfaceHolder = holder;
		this.mSurfaceHolder.addCallback(this);
		this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.mPreviewCallback = previewCallback;
		this.mActivity = activity;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		startPreView();
		Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		adjustSize();
		openCamera();
		Log.d(TAG, "surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		closeCamera();
		Log.d(TAG, "surfaceDestroyed");
	}
	
	/**
	 * @return id 转换的cameraid
	 */
	public int switchCamera() {
		int num = Camera.getNumberOfCameras();
		for (int i = 0; i < num; i++) {
			if (i != mOpenCameraId) {
				stopPreview();
				closeCamera();
				openCamera(i);
				startPreView();
				return i;
			}
		}
		return 0;
	}

	/**
	 * 获取已经打开的摄像头
	 * 
	 * @return
	 */
	public Camera getOpenCamera() {
		return mOpenCamera;
	}

	/**
	 * 打开默认摄像头
	 */
	public void openCamera() {
		int cameraId = CameraHelper.getFutureCameraId(mActivity);
		openCamera(cameraId);
	}

	/**
	 * 打开指定的摄像头
	 * 
	 * @param cameraId
	 */
	public void openCamera(int cameraId) {
		// 指定的摄像头跟已打开的摄像头一致,不在重复打开
		if (mOpenCamera != null && mOpenCameraId == cameraId) {
			return;
		}
		// 其他摄像头 正在用，关闭其他摄像头
		else if (mOpenCamera != null) {
			closeCamera();
		}
		openRealCamera(cameraId);
	}

	private void openRealCamera(int cameraId) {
		mOpenCameraId = cameraId;
		mOpenCamera = Camera.open(mOpenCameraId);
	}

	/**
	 * 开始预览
	 */
	public void startPreView() {

		if (mOpenCamera == null)
			return;

		try {
			mOpenCamera.setPreviewDisplay(mSurfaceHolder);
		} catch (IOException e) {
			closeCamera();
			return;
		}
		int[] futurePreviewSize = CameraHelper.getFuturePreviewSize(mActivity, mOpenCamera); // 设置预览分辨率
		Camera.Parameters parameters = mOpenCamera.getParameters();
		sPreviewWidth = futurePreviewSize[0];
		sPreViewHeight = futurePreviewSize[1];
		ULog.i(TAG, "width:" + sPreviewWidth);
		parameters.setPreviewSize(sPreviewWidth, sPreViewHeight);
		parameters.setPictureSize(sPreviewWidth, sPreViewHeight);
		mOpenCamera.setParameters(parameters);
		final int N = 20;
		final int SIZE = sPreviewWidth * sPreViewHeight * 3 / 2;
		for (int i = 0; i < N; i++) {
			byte[] callbackBuffer = new byte[SIZE];
			mOpenCamera.addCallbackBuffer(callbackBuffer);
		}
		mOpenCamera.setPreviewCallbackWithBuffer(mPreviewCallback);
		mOrientation = CameraHelper.getDisplayRotation(
				mActivity, mOpenCameraId);
		mOpenCamera.setDisplayOrientation(mOrientation);
		mOpenCamera.startPreview();

	}

	/**
	 * 结束预览
	 */
	public void stopPreview() {
		if (mOpenCamera != null) {
			mOpenCamera.stopPreview();
		}
	}

	/**
	 * 关闭摄像头
	 */
	public void closeCamera() {
		if (mOpenCamera == null) {
			return;
		}
		mOpenCamera.setPreviewCallback(null);
		mOpenCamera.stopPreview();
		mOpenCamera.release();
		mOpenCameraId = -1;
		mOpenCamera = null;
		Log.i("SFHCamera", "the camera is close");
	}

	/**
	 * 是否是前置摄像头
	 * @return true 前置摄像头， false 后置摄像头。
	 */
	public boolean facingFront() {
		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(mOpenCameraId, cameraInfo);
		return cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT;
	}
	
	/**
	 * 获取当前camera的图片需要旋转的角度
	 * @return 图片旋转角度
	 */
	public int getImageRotation() {
		return CameraHelper.getImageRotation(mActivity, mOpenCameraId);
	}

	/**
	 * 设置需要调整与摄像长宽比例的view（比如用来显示摄像头内容的view，保证显示的时候画面不失真）
	 * 
	 * @param view
	 */
	public void setAdjustView(View view) {
		this.mAdjustView = view;
	}

	/**
	 * 根据摄像头的预览的长宽比来调整的界面显示区域的长宽比。
	 */
	public void adjustSize() {
		if (mAdjustView == null) {
			return;
		}
		ViewGroup.LayoutParams params = mAdjustView.getLayoutParams();
		float width = mAdjustView.getWidth();
		float height = mAdjustView.getHeight();
		float ratio = width / height;
		float cameraRatio;
		if (mOrientation == 0 || mOrientation == 180) {
			cameraRatio = (float) sPreviewWidth / (float) sPreViewHeight;
		} else {
			cameraRatio = (float) sPreViewHeight / (float) sPreviewWidth;
		}
		
		if (mAdjustType == ADJUST_VIEW_BY_RIATIO) {
			// 调整width
			if (ratio > cameraRatio) {
				width = height * cameraRatio;
			} else {
				height = width / cameraRatio;
			}
		}
		if (mAdjustType == ADJUST_VIEW_WIDTH) {
			width = height * cameraRatio;
		}
		
		if (mAdjustType == ADJUST_VIEW_HEIGHT) {
			height = width / cameraRatio;
		}
		
		params.width = (int) width;
		params.height = (int) height;
		
		if (mAdjustView != null) {
			mAdjustView.setLayoutParams(params);
		}
	}

	public void doTakePicture(){
		/*if(isPreviewing && (mCamera != null)){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}*/
		ULog.i(TAG, "doTakePicture");
		if (mOpenCamera != null ){
			ULog.i(TAG, "doTakePicture1");
			mOpenCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}

	}

	public void doTakePicture(int typeNo){

		ULog.i(TAG, "doTakePicture typeNo");
		mTypeNo = typeNo;
		if (mOpenCamera != null ){
			mOpenCamera.takePicture(mShutterCallback, null, mJpegFaceGetCallback);
		}

	}

	/*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
			//快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	{
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.i(TAG, "myShutterCallback:onShutter...");
		}
	};
	Camera.PictureCallback mRawCallback = new Camera.PictureCallback()
			// 拍摄的未压缩原数据的回调,可以为null
	{

		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myRawCallback:onPictureTaken...");

		}
	};
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
		Matrix matrix = new Matrix();
		matrix.postRotate((float)rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}
	Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()
			//对jpeg图像数据的回调,最重要的一个回调
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mOpenCamera.stopPreview();
				isPreviewing = false;
			}


			//保存图片到sdcard
			if(null != b)
			{
				//设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
				//图片竟然不能旋转了，故这里要旋转下
				Bitmap rotaBitmap = getRotateBitmap(b, 270.0f);
				//if (CheckFaceUtil.getInstance().isFaceBitMap(rotaBitmap)){
					FileUtil.saveBitmap(rotaBitmap);
				//}

			}
			//再次进入预览
			mOpenCamera.startPreview();
			isPreviewing = true;
		}
	};

	Camera.PictureCallback mJpegFaceGetCallback = new Camera.PictureCallback()
			//对jpeg图像数据的回调,最重要的一个回调
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.i(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mOpenCamera.stopPreview();
				isPreviewing = false;
			}


			//保存图片到sdcard
			if(null != b)
			{
				//设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
				//图片竟然不能旋转了，故这里要旋转下
				Bitmap rotaBitmap = getRotateBitmap(b, 270.0f);
				//if (CheckFaceUtil.getInstance().isFaceBitMap(rotaBitmap)){
				FileUtil.saveBitmap(rotaBitmap, mTypeNo);
				//}

			}
			//再次进入预览
			mOpenCamera.startPreview();
			isPreviewing = true;
		}
	};
}
