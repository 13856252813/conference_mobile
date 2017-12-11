package com.tofu.conference.utils

import android.content.Context

/**
 * Created by pc on 2017/4/13.
 */

object DensityUtils {


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 获取屏幕高度
     */
    fun getHeight(context: Context): Int {
        val height = context.resources.displayMetrics.heightPixels.toFloat()
        return height.toInt()
    }

    /**
     * 获取屏幕宽度
     */
    fun getWidth(context: Context): Int {
        val width = context.resources.displayMetrics.widthPixels.toFloat()
        return width.toInt()
    }
}
