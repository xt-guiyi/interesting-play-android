package com.xtguiyi.loveLife.utils

import java.util.Locale

object CommonUtil {
    /**
     *  格式化数字(10,1000,1万，10万，,100万)
     *  @param value 数字
     *  */
    fun formatNumber(value: Long): String {
        return if (value >= 10000) {
            String.format(Locale.getDefault(),"%.1f万", value / 10000.0)
        } else {
            value.toString()
        }
    }
}