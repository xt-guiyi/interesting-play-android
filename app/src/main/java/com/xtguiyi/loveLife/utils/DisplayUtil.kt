package com.xtguiyi.loveLife.utils

import android.content.Context
import android.util.TypedValue

object DisplayUtil {
    /**
     * 获取设备屏幕高度，单位为像素
     * */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取设备屏幕宽度，单位为像素
     * */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕密度
     * */
    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 获取屏幕密度DPI
     * */
    fun getDensityDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }


    /**
     * dp转px
     * */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return ((dpValue * scale) + 0.5f).toInt()
    }

    /**
     * px转dp
     * */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return ((pxValue -  0.5f) / scale).toInt()
    }

    /**
     * 将sp转换为px
     * */
    fun spToPx(context: Context, spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 将px转换为sp
     * */
    fun pxToSp(context: Context, pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.density).toInt();
    }
}