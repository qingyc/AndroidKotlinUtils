package com.qing.androidkotlinutils.core.utils


import com.qing.androidkotlinutils.core.R
import kotlin.math.roundToInt

object TimeUtil {
    //天
    const val DAY_MINISECOND = 86400000L//天
    //时
    const val HOUR_MINISECOND = 3600000L//
    //分
    const val MINITE_MINISECOND = 60000L
    //秒
    const val SECOND_MINISECOND = 1000L


    fun millis2FitTimeSpan(time: Long, showAllUnit: Boolean = false): String {

        val sb = StringBuilder()
        val hour = time / HOUR_MINISECOND
        val minute = time % HOUR_MINISECOND / MINITE_MINISECOND
        val fl = time % MINITE_MINISECOND.toFloat() / SECOND_MINISECOND
        val second = if (fl < 59) fl.roundToInt() else fl.toInt()
        if (showAllUnit) {
            sb.append(hour).append(ResUtils.getString(R.string.unit_hour))
            sb.append(minute).append(ResUtils.getString(R.string.unit_minute))
            sb.append(second).append(ResUtils.getString(R.string.second))
        } else {
            if (hour > 0) {
                sb.append(hour).append(ResUtils.getString(R.string.unit_hour))
            }
            if (minute > 0) {
                sb.append(minute).append(ResUtils.getString(R.string.unit_minute))
            }
            if (hour == 0L && minute == 0L) {
                sb.append(second).append(ResUtils.getString(R.string.second))
            } else {
                if (hour == 0L || minute == 0L) {
                    if (second > 0) {
                        sb.append(second).append(ResUtils.getString(R.string.second))
                    }
                }
            }

        }
        return sb.toString()
    }


    fun millis2FitTimeSpanByMinute(timeMinute: Int): String {

        val sb = StringBuilder()
        val hour = timeMinute / 60
        val minute = timeMinute % 60
        if (hour > 0) {
            sb.append(hour).append(ResUtils.getString(R.string.unit_hour))
        }
        if (hour == 0) {
            sb.append(minute).append(ResUtils.getString(R.string.unit_minute))
        } else {
            if (minute > 0) {
                sb.append(minute).append(ResUtils.getString(R.string.unit_minute))
            }
        }

        return sb.toString()
    }


}