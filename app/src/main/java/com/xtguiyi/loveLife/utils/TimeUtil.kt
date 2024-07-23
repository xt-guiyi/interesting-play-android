package com.xtguiyi.loveLife.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

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
     * @param second 秒
     */
    fun geDurationTime(second: Long): String {
        val hours = TimeUnit.SECONDS.toHours(second)
        val minutes = TimeUnit.SECONDS.toMinutes(second) % 60
        val secs = second % 60

        return if (hours > 0) {
            String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format(Locale.getDefault(),"%02d:%02d", minutes, secs)
        }
    }

    /**
     * 时间格式化(刚刚、几秒前、几分钟前、几小时前、几天前)
     * */
    fun getTimeAgo(time: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - time

        return when {
            diff < TimeUnit.SECONDS.toMillis(1) -> "刚刚"
            diff < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(diff)}秒前"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}分钟前"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}小时前"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)}天前"
        }
    }
}