package  com.qing.androidkotlinutils.core.ext

import android.graphics.Rect
import android.text.TextPaint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qingyc.qlogger.QLogger
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern


/**
 * 首字母大写
 * @receiver String
 * @return String
 */
fun String.toFirstUpperCase(): String {
    return substring(0, 1).toUpperCase(Locale.ENGLISH) + substring(1)
}

/**
 * 计算文本Rect
 * @receiver String
 * @param textSize Float
 * @return Rect
 */
fun String.calcStringRect(textSize: Float): Rect {
    val outRect = Rect()
    val textPaint = TextPaint()
    textPaint.textSize = textSize
    textPaint.getTextBounds(this, 0, length, outRect)
    return outRect
}


//---------------------------------------------------- log --------------------------------------------------------------------//

fun String.loge() {
    QLogger.e(this)
}

fun String.logi() {
    QLogger.i(this)

}


//---------------------------------------------------- Num Format or verify--------------------------------------------------------------------//

fun String.isInt(): Boolean {
    if (isNumber()) {
        return BigInteger(this).toLong() < Integer.MAX_VALUE && BigInteger(this).toLong() > Integer.MIN_VALUE
    }
    return false
}


fun String.isDecimal(): Boolean {
    if (this == "0") {
        return true
    }
    val pattern = Pattern.compile("-?([0-9]+\\.0*)?[1-9][0-9]*$")
    return pattern.matcher(this).find()
}

fun String.isNumber(): Boolean {
    if (this == "0") {
        return true
    }
    val pattern = Pattern.compile("-?[1-9][0-9]*$")
    return pattern.matcher(this).find()
}

fun String.isBoolean(): Boolean {
    return this.toLowerCase(Locale.ENGLISH) == "true" || this.toLowerCase(Locale.ENGLISH) == "false"
}

fun String.toBoolean(): Boolean {
    if (isBoolean()) {
        return this.toLowerCase(Locale.ENGLISH) == "true"
    }
    return false
}

fun String.toInt(): Int {
    if (isInt()) {
        return BigInteger(this).toInt()
    }
    return 0
}


fun String.toLong(): Long {
    if (isNumber()) {
        return BigInteger(this).toLong()
    }
    return 0L
}


fun String.toFloat(): Float {
    if (isDecimal()) {
        return BigDecimal(this).toFloat()
    }
    return 0F
}

fun String.toDouble(): Double {
    if (isDecimal()) {
        return BigDecimal(this).toDouble()
    }
    return 0.0
}


//---------------------------------------------------- color--------------------------------------------------------------------//


/**
 * 返回含有颜色值的html片段
 *
 * @param content
 * @param color
 */
fun String.setColorHtml(content: String, color: String): String {
    return buildString("<font color=", color, ">", content, "</font>")
}


fun buildString(vararg element: Any): String {
    val sb = StringBuffer()
    for (str in element)
        sb.append(str)
    return sb.toString()
}


fun String.getNumStr(canStartWithZero: Boolean = true): String {
    if (this.isEmpty()) {
        return "0"
    }
    var replace = this.replace(Regex("[^0-9]"), "")

    if (canStartWithZero) {
        return replace
    }
    while (replace.startsWith("0")) {
        replace = replace.substring(1, replace.length - 1)
    }
    return replace

}

fun String.safeToInt(defaultValue: Int = 0): Int {
    var num = defaultValue
    try {
        num = Integer.parseInt(this.getNumStr())
    } catch (e: Exception) {
    }
    return num
}


//---------------------------------------------------- json to obj--------------------------------------------------------------------//

inline fun <reified T> String.jsonToObj(): T? {
    return try {
        val g = Gson()
        val type = object : TypeToken<T>() {}.type
        g.fromJson<T>(this, type)
    } catch (e: Exception) {
        null
    }
}


inline fun <reified T> String.jsonToObjList(): ArrayList<T>? {
    return try {
        val g = Gson()
        val type = object : TypeToken<ArrayList<T>>() {}.type
        return g.fromJson<ArrayList<T>>(this, type)
    } catch (e: Exception) {
        null
    }
}