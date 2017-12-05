package com.reconova.faceid.utils;

import java.util.List;

import com.reconova.data.ImageStruct.ImageResult.FaceRect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;

public class DrawFaceRectUtil {
	/** 画笔 */
	private Paint mPaint;
	
	/** 绘制图片的时候，界面是否左右翻转  */
	private boolean mIsReverse = false;
	
	public DrawFaceRectUtil() {
		mPaint = new Paint();
		
		mPaint.setColor(Color.BLUE);
		
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		
		mPaint.setTextSize(30);
	}

	/**
	 * 设置画笔颜色
	 * @param color 颜色
	 */
	public void setUpPaintColor(int color) {
		mPaint.setColor(color);
	}

	/**
	 * 是否界面翻转
	 * @param isReverse 是否翻转
	 */
	public void setIsReverse(boolean isReverse) {
		this.mIsReverse = isReverse;
	}

	/**
	 * 在画布绘制人脸框
	 * @param canvas 画布
	 * @param rectList 人脸列表
	 * @param imageWidth 图片宽度
	 * @param imageHeight 图片高度
	 */
	public void drawFaces(Canvas canvas, List<FaceRect> rectList,
			int imageWidth, int imageHeight) {
		/** 清空画布*/
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		
		if (rectList == null) return;
		
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		
		/** 计算从 图片坐标 到  画布坐标的 缩放比例 */
		final float xScale = (float) canvasWidth  / (float) imageWidth;
		final float yScale = (float) canvasHeight / (float) imageHeight;
		
		for (FaceRect imageRect: rectList) {
			RectF canvasRect = new RectF();
			
			canvasRect.left   = (float) imageRect.mRect_left  * xScale;
			canvasRect.right  = (float) imageRect.mRect_right * xScale;
			canvasRect.top	  = (float) imageRect.mRect_top    * yScale;
			canvasRect.bottom = (float) imageRect.mRect_bottom * yScale;
			
			/** 水平方向翻转界面 */
			if (mIsReverse) {
				canvasRect.left  = canvasWidth - canvasRect.left;
				canvasRect.right = canvasWidth - canvasRect.right;
			}
			canvas.drawRect(canvasRect, mPaint);
		}
	}

}
