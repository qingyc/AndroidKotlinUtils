package com.qing.androidkotlinutils.core.utils

import android.text.format.DateUtils
import com.qing.androidkotlinutils.core.R
import com.qingyc.qlogger.QLogger
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {


    const val DATE_FORMART_yyyyMMdd = "yyyyMMdd"

    /**
     * 一天多少毫秒
     */
    const val ONE_DAY_MILLISECONDS = 24 * 60 * 60 * 1000L
    const val ONE_HOUR_MILLISECONDS = 60 * 60 * 1000L
    const val ONE_MINUTE_MILLISECONDS = 60 * 1000L


    //获取当日00:00:00的时间戳,东八区则为早上八点
    fun getZeroClockTimestamp(time: Long): Long {
        var currentStamp = time
        currentStamp -= currentStamp % DateUtils.DAY_IN_MILLIS
        QLogger.i("DateTransHelper", " DateTransUtils-getZeroClockTimestamp()  获取当日00:00:00的时间戳 :${Date(currentStamp)}")
        return currentStamp
    }

    /**
     * 获取月份名
     */
    fun getMonthName(month: Int, locale: Locale = Locale.getDefault()): String? {
        // 超出月份限制
        if (month !in (1..12)) return null

        // 重置月份
        val cal = Calendar.getInstance()
        // 设置日期为一号，防止没有31号的月份，无法设置
        cal.set(Calendar.MILLISECOND, 1)
        cal.set(Calendar.MONTH, (month - 1))
        val finalLocale = if (locale.language != "hi") Locale.ENGLISH else locale
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, finalLocale)
    }

    /**
     *获取一月多少天
     */
    fun getMaxDayOfCurrentMonth() = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

    /**
     * 当前日期
     */
    fun getDay(time: Long = -1L): Int {
        val instance = Calendar.getInstance()
        if (time != -1L) {
            instance?.time = Date(time)
        }
        return instance?.get(Calendar.DAY_OF_MONTH) ?: 1
    }


    /**
     * 当前年份
     */
    fun getYear(time: Long = -1): Int {

        val instance = Calendar.getInstance()
        if (time != -1L) {
            instance?.time = Date(time)
        }
        return instance?.get(Calendar.YEAR) ?: 1

    }

    /**
     * 获取当前月份
     */
    fun getMonth(time: Long = -1): Int {

        val instance = Calendar.getInstance()
        if (time != -1L) {
            instance?.time = Date(time)
        }
        return (instance?.get(Calendar.MONTH) ?: 0) + 1
    }

    fun getCurrentHour() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    /**
     * 获取日期 格式:20181113
     */
    fun getDate(time: Long = System.currentTimeMillis()): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.US) //20181113
        return simpleDateFormat.format(Date(time))
    }

    fun getHourAndMinute(time: Long = System.currentTimeMillis()): String {
        val simpleDateFormat = SimpleDateFormat("kk:mm", Locale.US)
        return simpleDateFormat.format(Date(time))
    }

    fun getDate(time: Long = System.currentTimeMillis(), format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.US) //20181113
        return simpleDateFormat.format(Date(time))
    }

    fun getYesterdayDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        return simpleDateFormat.format(Date(System.currentTimeMillis() - ONE_DAY_MILLISECONDS))
    }


    /**
     * 0时的时间long
     */
    fun getTodayStartTime(): Long {
        //获取今天年月日
        val currentDay = CalendarUtil.getDay()
        val currentMonth = CalendarUtil.getMonth()
        val currentYear = CalendarUtil.getYear()
        val instance = Calendar.getInstance()
        instance.clear()
        //设置时间为今天开始时间
        instance.set(Calendar.DAY_OF_MONTH, currentDay)
        instance.set(Calendar.MONTH, currentMonth - 1)
        instance.set(Calendar.YEAR, currentYear)
        instance.set(Calendar.HOUR_OF_DAY, 0)
        instance.set(Calendar.MINUTE, 0)
        instance.set(Calendar.SECOND, 0)
        instance.set(Calendar.MILLISECOND, 0)
        //今天开始 long
        val data = instance.time
        return data.time
    }


    fun getTimeLongByyyyyMMdd(timeStr: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        val date = simpleDateFormat.parse(timeStr)
        return date?.time ?: -1L
    }


    fun getDayOfMonth(time: Long = -1L): Int {
        val instance = Calendar.getInstance()
        if (time != -1L) {
            instance.time = Date(time)
        }
        return instance.get(Calendar.DAY_OF_MONTH)
    }


    fun getDayOfYear(time: Long = -1L): Int {
        val instance = Calendar.getInstance()
        if (time != -1L) {
            instance.time = Date(time)
        }
        return instance.get(Calendar.DAY_OF_YEAR)
    }


    /**
     * 获取这周的日期(从星期天开始)
     */
    fun getDaysInCurrentWeek(): ArrayList<String> {
        val thisWeekDayList = arrayListOf<String>() //20180911
        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.DAY_OF_WEEK) //1 是星期天
        val todayTimeLong = calendar.time.time

        for (i in 0..6) {
            //获取
            //一周第一天
            val time = todayTimeLong - (week - 1 - i) * ONE_DAY_MILLISECONDS
            thisWeekDayList.add(getDate(time))
        }
        return thisWeekDayList
    }


    fun formatMonthAndDay(day: Int): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val format = ResUtils.getString(R.string.format_date_no_year)
        val formatStr = SimpleDateFormat(format, Locale.US).format(calendar.time)
        return formatStr ?: ""
    }


    fun getFormatMonthAndDay(timeLong: Long): String {
        val format = ResUtils.getString(R.string.format_date_no_year)
        val formatStr = SimpleDateFormat(format, Locale.US).format(Date(timeLong))
        return formatStr ?: ""


    }

//    /**
//     * 计算选中时间和系统时间之间相差多少天
//     */
//    fun dayAfterToday(year: Int, month: Int, day: Int): Int {
//        val instance = Calendar.getInstance()
//        instance.set(Calendar.YEAR, year)
//        instance.set(Calendar.MONTH, month - 1)
//        instance.set(Calendar.DAY_OF_MONTH, day)
//        val date = instance.time
//        val timeSelected = date.time
//        instance.set(Calendar.YEAR, CalendarUtil.getCurrentYear2018())
//        instance.set(Calendar.MONTH, CalendarUtil.getMonth() - 1)
//        instance.set(Calendar.DAY_OF_MONTH, CalendarUtil.getDay())
//        val date2 = instance.time
//        val timeNOw = date2.time
//        val dayLong = (timeSelected - timeNOw) / (1000 * 60 * 60 * 24)
//        return dayLong.toInt()
//    }


    /**
     * 获取星期
     */
//    fun getWeekDay(year: Int, month: Int, day: Int): String {
//        val calendarInstance: Calendar = Calendar.getInstance()
//        calendarInstance.set(year, month - 1, day)
//        val week = calendarInstance[Calendar.DAY_OF_WEEK]
//        //星期文本
//        return when (week) {
//            1 -> {
//                sApp.ResUtils.getString(R.string.sunday)
//            }
//            2 -> {
//                sApp.ResUtils.getString(R.string.monday)
//            }
//            3 -> {
//                sApp.ResUtils.getString(R.string.tuesday)
//            }
//            4 -> {
//                sApp.ResUtils.getString(R.string.wednesday)
//            }
//            5 -> {
//                sApp.ResUtils.getString(R.string.thursday)
//            }
//            6 -> {
//                sApp.ResUtils.getString(R.string.friday)
//            }
//            else -> {
//                sApp.ResUtils.getString(R.string.saturday)
//            }
//        }
//
//    }
}
