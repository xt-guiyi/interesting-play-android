package com.xtguiyi.play.utils

import android.util.TypedValue
import com.xtguiyi.play.MainApplication

object DisplayUtil {
    /**
     * 获取设备屏幕高度，单位为像素
     * */
    fun getScreenHeight(): Int {
        return MainApplication.context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取设备屏幕宽度，单位为像素
     * */
    fun getScreenWidth(): Int {
        return MainApplication.context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕密度
     * */
    fun getDensity(): Float {
        return MainApplication.context.resources.displayMetrics.density
    }

    /**
     * 获取屏幕密度DPI
     * */
    fun getDensityDpi(): Int {
        return MainApplication.context.resources.displayMetrics.densityDpi
    }


    /**
     * dp转px
     * */
    fun dip2px(dpValue: Float): Int {
        val scale = MainApplication.context.resources.displayMetrics.density
        return ((dpValue * scale) + 0.5f).toInt()
    }

    /**
     * px转dp
     * */
    fun px2dip(pxValue: Float): Int {
        val scale = MainApplication.context.resources.displayMetrics.density
        return ((pxValue -  0.5f) / scale).toInt()
    }

    /**
     * 将sp转换为px
     * */
    fun spToPx(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            MainApplication.context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 将px转换为sp
     * */
    fun pxToSp(pxValue: Float): Int {
        return (pxValue / MainApplication.context.resources.displayMetrics.density).toInt()
    }
}