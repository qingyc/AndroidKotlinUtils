package com.qing.androidkotlinutils.core.utils


import com.qingyc.qlogger.QLogger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * 类说明: 星座
 *
 * @author qing
 * @time 2019-04-22 10:31
 */
object HoroscopeUtil {

    //static final String[] HOROSCOPES_ARR = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座", "水瓶座", "双鱼座"};
    /**
     * 星座名
     */
    private val HOROSCOPES_ARR = arrayOf("aries", "taurus", "gemini", "cancer", "leo", "virgo", "libra", "scorpio", "sagittarius", "capricorn", "aquarius", "pisces")
    /**
     * 星座名 印地语
     */
    private val HOROSCOPES_HINDI_ARR = arrayOf("मेष", "वृषभ", "मिथुन", "कर्क", "सिंह", "कन्या", "तुला", "वृश्चिक", "धनु", "मकर", "कुंभ", "कुंभ")

    fun getHoroscopeName(time: String): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val constellation = ""
        try {
            val parse = simpleDateFormat.parse(time)
            calendar.time = parse
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            QLogger.i("month==$month day===$day")
        } catch (e: ParseException) {
            QLogger.e(e)
        }

        return constellation
    }


    //    十二星座划分
    //    白羊座（ 3.21------4.19 ）
    //    金牛座（ 4.20------5.20）
    //    双子座（ 5.21------6.20）
    //    巨蟹座（ 6.21------7.22）
    //    狮子座（ 7.23------8.22）
    //    处女座（ 8.23------9.22）
    //    天秤座（ 9.23------10.22）
    //    天蝎座（10.23-----11.21）
    //    射手座（11.23-----12.21）
    //    摩羯座（12.22-----1.19）
    //    水瓶座（ 1.20-----2.18）
    //    双鱼座（ 2.19-----3.20）

    fun getHoroscopeName(month: Int, day: Int): String {
        var point = -1
        val formatDay = String.format(Locale.US, "%02d", day)
        val date = java.lang.Double.parseDouble("$month.$formatDay")
        if (date in 3.21..4.19) {
            point = 0
        } else if (date in 4.20..5.20) {
            point = 1
        } else if (date in 5.21..6.20) {
            point = 2
        } else if (date in 6.21..7.22) {
            point = 3
        } else if (date in 7.23..8.22) {
            point = 4
        } else if (date in 8.23..9.22) {
            point = 5
        } else if (date in 9.23..10.22) {
            point = 6
        } else if (date in 10.23..11.21) {
            point = 7
        } else if (date in 11.22..12.21) {
            point = 8
        } else if (date in 12.22..12.31) {
            point = 9
        } else if (date in 1.01..1.19) {
            point = 9
        } else if (date in 1.20..2.18) {
            point = 10
        } else if (date in 2.19..3.20) {
            point = 11
        }
        return if (point == -1) {
            HOROSCOPES_ARR[2]
        } else HOROSCOPES_ARR[point]
    }

    fun getHindiHoroscopeName(month: Int, day: Int): String {
        var point = -1
        val formatDay = String.format(Locale.US, "%02d", day)
        val date = java.lang.Double.parseDouble("$month.$formatDay")
        if (date in 3.21..4.19) {
            point = 0
        } else if (date in 4.20..5.20) {
            point = 1
        } else if (date in 5.21..6.20) {
            point = 2
        } else if (date in 6.21..7.22) {
            point = 3
        } else if (date in 7.23..8.22) {
            point = 4
        } else if (date in 8.23..9.22) {
            point = 5
        } else if (date in 9.23..10.22) {
            point = 6
        } else if (date in 10.23..11.21) {
            point = 7
        } else if (date in 11.22..12.21) {
            point = 8
        } else if (date in 12.22..12.31) {
            point = 9
        } else if (date in 1.01..1.19) {
            point = 9
        } else if (date in 1.20..2.18) {
            point = 10
        } else if (date in 2.19..3.20) {
            point = 11
        }
        return if (point == -1) {
            HOROSCOPES_HINDI_ARR[2]
        } else HOROSCOPES_HINDI_ARR[point]
    }


    fun getHoroscopeIndex(month: Int, day: Int): Int {
        var point = 1
        val formatDay = String.format(Locale.US, "%02d", day)
        val date = java.lang.Double.parseDouble("$month.$formatDay")
        if (date in 3.21..4.19) {
            point = 0
        } else if (date in 4.20..5.20) {
            point = 1
        } else if (date in 5.21..6.21) {
            point = 2
        } else if (date in 6.22..7.22) {
            point = 3
        } else if (date in 7.23..8.22) {
            point = 4
        } else if (date in 8.23..9.22) {
            point = 5
        } else if (date in 9.23..10.23) {
            point = 6
        } else if (date in 10.24..11.22) {
            point = 7
        } else if (date in 11.23..12.21) {
            point = 8
        } else if (date in 12.22..12.31) {
            point = 9
        } else if (date in 1.01..1.19) {
            point = 9
        } else if (date in 1.20..2.18) {
            point = 10
        } else if (date in 2.19..3.20) {
            point = 11
        }
        return point
    }

    fun getHoroscopeIndex(horoscopeName: String): Int {
        for (i in HOROSCOPES_ARR.indices) {
            if (HOROSCOPES_ARR[i].equals(horoscopeName, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }
}
