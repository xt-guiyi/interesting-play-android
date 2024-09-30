package com.xtguiyi.loveLife.utils

import android.content.Context
import android.util.TypedValue
import com.xtguiyi.loveLife.MainApplication

object DisplayUtil {
    /**
     * 获取设备屏幕高度，单位为像素
     * */
    fun getScreenHeight(): Int {
        return MainApplication.getContext().resources.displayMetrics.heightPixels
    }

    /**
     * 获取设备屏幕宽度，单位为像素
     * */
    fun getScreenWidth(): Int {
        return MainApplication.getContext().resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕密度
     * */
    fun getDensity(): Float {
        return MainApplication.getContext().resources.displayMetrics.density
    }

    /**
     * 获取屏幕密度DPI
     * */
    fun getDensityDpi(): Int {
        return MainApplication.getContext().resources.displayMetrics.densityDpi
    }


    /**
     * dp转px
     * */
    fun dip2px(dpValue: Float): Int {
        val scale = MainApplication.getContext().resources.displayMetrics.density
        return ((dpValue * scale) + 0.5f).toInt()
    }

    /**
     * px转dp
     * */
    fun px2dip(pxValue: Float): Int {
        val scale = MainApplication.getContext().resources.displayMetrics.density
        return ((pxValue -  0.5f) / scale).toInt()
    }

    /**
     * 将sp转换为px
     * */
    fun spToPx(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            MainApplication.getContext().resources.displayMetrics
        ).toInt()
    }

    /**
     * 将px转换为sp
     * */
    fun pxToSp(pxValue: Float): Int {
        return (pxValue / MainApplication.getContext().resources.displayMetrics.density).toInt()
    }
}