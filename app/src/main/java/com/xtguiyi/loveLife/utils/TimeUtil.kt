package com.xtguiyi.loveLife.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object TimeUtil {

    /**
     * 格式化当前日期
     * @param timestamp 时间戳
     * @param pattern 格式
     */
    fun getCurrentFormattedDate(timestamp: Long? = null, pattern: String?  = "yyyy-MM-dd HH:mm:ss"): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = timestamp?.let { Date(it) } ?: Date()
        return dateFormat.format(date)
    }


    /**
     * 时间格式化( 02:00、01:01:00)
     * @param mSecond 秒
     */
    fun geDurationTime(mSecond: Long): String {
        val duration = mSecond.seconds
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60
        val secs = duration.inWholeSeconds % 60

        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, secs)
        } else {
            "%02d:%02d".format(minutes, secs)
        }
    }

    /**
     * 时间格式化(刚刚、几秒前、几分钟前、几小时前、几天前)
     * @param timeStamp 时间戳
     * */
    fun getTimeAgo(timeStamp: Long): String {
        val diff = (System.currentTimeMillis() - timeStamp).milliseconds
        return when {
            diff < 1.seconds -> "刚刚"
            diff < 1.minutes -> "${diff.inWholeSeconds}秒前"
            diff < 1.hours -> "${diff.inWholeMinutes}分钟前"
            diff < 1.days -> "${diff.inWholeHours}小时前"
            diff in 1.days..2.days -> {
                val remainMillis = (2.days - diff)
                val hours = remainMillis.inWholeHours.toString().padStart(2, '0')
                val minutes = (remainMillis.inWholeMinutes % 60).toString().padStart(2, '0')
                "昨天 $hours:$minutes"
            }
            else -> "${diff.inWholeDays}天前"
        }
    }
}