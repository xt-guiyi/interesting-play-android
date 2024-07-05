package com.example.lovelife.utils

import java.util.Calendar

object TimeUtils {
    /**
     * 时间格式化(刚刚、几秒前、几分钟前、几小时前、几天前)
     * */
    fun calculate(timeStamp: Long): String {
        var time = timeStamp
        val timeInMillis = Calendar.getInstance().timeInMillis

        //兼容脏数据。抓取的数据有些帖子的时间戳不是标准的十三位
        val valueOf = time.toString()
        if (valueOf.length < 13) {
            time *= 1000
        }
        val diff = (timeInMillis - time) / 1000
        return if (diff <= 5) {
            "刚刚"
        } else if (diff < 60) {
            diff.toString() + "秒前"
        } else if (diff < 3600) {
            (diff / 60).toString() + "分钟前"
        } else if (diff < 3600 * 24) {
            (diff / (3600)).toString() + "小时前"
        } else {
            (diff / (3600 * 24)).toString() + "天前"
        }
    }
}