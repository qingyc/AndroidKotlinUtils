package com.qing.androidkotlinutils.core.utils

import android.text.TextUtils
import com.qingyc.qlogger.QLogger
import org.json.JSONObject
import java.util.*
/**
 *
 * 类说明: 字符串工具
 *
 * @author qing
 * @time 2018/6/14 10:36
 */
object StringUtil {

    fun stringList2JsonString(stringList: List<String>?): String? {

        try {
            val json = JSONObject()
            if (stringList != null) {
                for (i in stringList.indices) {
                    val key = i.toString()
                    json.put(key, stringList[i])
                }
            }
            return json.toString()
        } catch (e: Exception) {
            QLogger.e(e)
        }

        return null
    }

    fun jsonString2StringList(jsonString: String): List<String>? {

        if (TextUtils.isEmpty(jsonString)) {
            return null
        }

        try {
            val json = JSONObject(jsonString)

            val stringList = ArrayList<String>()

            val iterator = json.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                stringList.add(json.get(key).toString())
            }

            return stringList

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return null
    }

    /**
     * @param stringMap
     * @return
     */
    fun stringMap2JsonString(stringMap: Map<String, String>?): String? {

        try {
            val json = JSONObject()

            if (stringMap != null) {
                val entrySet = stringMap.entries
                for ((key, value) in entrySet) {
                    json.put(key, value)
                }
            }

            return json.toString()

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return null
    }

    fun jsonString2StringMap(jsonString: String): Map<String, String>? {

        if (TextUtils.isEmpty(jsonString)) {
            return null
        }

        try {
            val json = JSONObject(jsonString)

            val stringMap = HashMap<String, String>()

            val iterator = json.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                stringMap[key] = json.getString(key)
            }

            return stringMap

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return null
    }

    fun jsonString2JSONObject(jsonString: String): JSONObject? {

        if (TextUtils.isEmpty(jsonString)) {
            return null
        }

        try {

            return JSONObject(jsonString)

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return null
    }

    /**
     * @param stringList
     * @param splitString
     * @return
     */
    @JvmOverloads
    fun stringList2String(stringList: List<String>?, splitString: String = ","): String {

        if (stringList == null || stringList.isEmpty()) {
            return ""
        }

        try {
            val builder = StringBuilder()

            for (i in stringList.indices) {

                if (i != 0) {
                    builder.append(splitString)
                }

                builder.append(stringList[i])

            }

            return builder.toString()

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return ""
    }

    /**
     * @param stringMap
     * @param splitString
     * @return
     */
    @JvmOverloads
    fun stringMapKey2String(stringMap: Map<String, String>?, splitString: String = ","): String {

        if (stringMap.isNullOrEmpty()) {
            return ""
        }

        try {
            val builder = StringBuilder()

            val keys = stringMap.keys
            for ((index, key) in keys.withIndex()) {
                if (index != 0) {
                    builder.append(splitString)
                }

                builder.append(key)
            }

            return builder.toString()

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return ""
    }

    /**
     * @param stringMap
     * @param splitString
     * @return
     */
    @JvmOverloads
    fun stringMapValue2String(stringMap: Map<String, String>?, splitString: String = ","): String {

        if (stringMap.isNullOrEmpty()) {
            return ""
        }
        try {
            val builder = StringBuilder()

            val values = stringMap.values
            for ((index, value) in values.withIndex()) {
                if (index != 0) {
                    builder.append(splitString)
                }

                builder.append(value)
            }

            return builder.toString()

        } catch (e: Exception) {
            QLogger.e(e)
        }

        return ""
    }

    fun printStringMap(stringMap: Map<String, String>?) {
        val builder = StringBuilder()


        if (stringMap.isNullOrEmpty()) {
            return
        }


        var index = 0
        val entrySet = stringMap.entries
        for ((key, value) in entrySet) {
            if (index++ != 0) {
                builder.append(", ")
            }

            builder.append(key).append(":").append(value)
        }

        val content = builder.toString()
        QLogger.i("print: $content")
    }


    /**
     * 格式化json字符串 用于打印
     *
     * @param jsonStr
     * @return jsonStr
     */
    fun formatJson(jsonStr: String): String {
        var level = 0
        val jsonForMatStr = StringBuffer()
        for (i in 0 until jsonStr.length) {
            val c = jsonStr[i]
            if (level > 0 && '\n' == jsonForMatStr[jsonForMatStr.length - 1]) {
                jsonForMatStr.append(getLevelStr(level))
            }
            when (c) {
                '{', '[' -> {
                    jsonForMatStr.append(c + "\n")
                    level++
                }
                ',' -> jsonForMatStr.append(c + "\n")
                '}', ']' -> {
                    jsonForMatStr.append("\n")
                    level--
                    jsonForMatStr.append(getLevelStr(level))
                    jsonForMatStr.append(c)
                }
                else -> jsonForMatStr.append(c)
            }
        }

        return jsonForMatStr.toString()

    }

    private fun getLevelStr(level: Int): String {
        val levelStr = StringBuffer()
        for (levelI in 0 until level) {
            levelStr.append("\t")
        }
        return levelStr.toString()
    }

    fun buildString(vararg element: Any): String {
        val sb = StringBuffer()
        for (str in element)
            sb.append(str)
        return sb.toString()
    }
}
