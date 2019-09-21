package com.qing.androidkotlinutils.core.utils

import android.preference.PreferenceManager
import com.blankj.utilcode.util.GsonUtils
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy

/**
 *
 * 类说明: sp 工具
 *
 * @author qing
 * @time 27/04/2018 15:52
 */
object SPUtil {

    /**
     * 是否同意隐私协议
     */
    const val KEY_AGREE_PRIVACY = "KEY_AGREE_PRIVACY"
    /**
     * 取消评价
     */
    const val KEY_COMMENT_CANCEL = "KEY_COMMENT_CANCEL"

    /**
     *是否是一次启动
     */
    const val KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH"
    /**
     * 安装时间
     */
    const val KEY_FIRST_LAUNCH_TIME = "KEY_FIRST_LAUNCH_TIME"
    /**
     *第一次评分
     */
    const val KEY_FIRST_RATE_TIME = "KEY_FIRST_RATE_TIME"
    /**
     * 新手引导
     */
    const val KEY_HAS_SHOW_FRESH_GUIDE = "KEY_HAS_SHOW_FRESH_GUIDE"
    /**
     * 通知发送时间
     */
    const val KEY_NOTIFICATION_HOUR = "KEY_NOTIFICATION_HOUR"
    /**
     * 评论次数(最多两次)
     */
    const val KEY_RATE_COUNT = "KEY_RATE_COUNT"
    /**
     * 是否显示评论dialog(评论5星或者3次非5星后停止)
     */
    const val KEY_SHOW_COMMENT_DIALOG = "KEY_SHOW_COMMENT_DIALOG"
    var lastAddTime = 0L
    /**
     * 今日触发次数加一
     */
    fun addActiveCountInToday(key: String, delayTime: Long = 600L): Int {
        if (key.isEmpty()) {
            return 0
        }
        //触发次数
        var activeCount = 0
        val loadOptionString = loadOptionString(key, "")
        val split = loadOptionString.split("-")
        if (split.size >= 2) {
            if (split[0] == CalendarUtil.getDate()) {
                try {
                    activeCount = Integer.parseInt(split[1])
                } catch (e: Exception) {
                }
            }
        }

        val currentTimeMillis = System.currentTimeMillis()
        if (delayTime >= 0 && lastAddTime != 0L) {
            if (currentTimeMillis - lastAddTime < delayTime) {
                lastAddTime = currentTimeMillis
                return activeCount
            }
        }
        lastAddTime = currentTimeMillis
        activeCount++
        savePrefs(key, CalendarUtil.getDate() + "-" + activeCount)
        return activeCount
    }

    // 是否包含key
    fun containsOption(key: String): Boolean {
        val prefs = getSP()
        return prefs.contains(key)
    }

    /**
     * 今日触发次数
     */
    fun loadActiveCountInToday(key: String): Int {
        var activeCount = 0
        val loadOptionString = loadOptionString(key, "")
        val split = loadOptionString.split("-")
        if (split.size >= 2) {
            if (split[0] == CalendarUtil.getDate()) {
                try {
                    activeCount = Integer.parseInt(split[1])
                } catch (e: Exception) {
                }
            }
        }
        return activeCount
    }

    fun <T> loadObj(key: String, type: Class<T>): T? {
        val loadOptionString = loadOptionString(key)
        return GsonUtils.fromJson(loadOptionString, type)
    }

    fun loadOptionBoolean(
        name: String,
        defValue: Boolean = false
    ): Boolean {
        val prefs = getSP()

        return prefs.getBoolean(name, defValue)
    }

    fun loadOptionFloat(
        name: String,
        defValue: Float
    ): Float {
        val prefs = getSP()

        return prefs.getFloat(name, defValue)
    }

    fun loadOptionInt(name: String, defValue: Int): Int {
        val prefs = getSP()
        return prefs.getInt(name, defValue)
    }

    fun loadOptionLong(
        name: String,
        defValue: Long
    ): Long {
        val prefs = getSP()

        return prefs.getLong(name, defValue)
    }

    fun loadOptionString(
        name: String,
        defValue: String = ""
    ): String {
        val prefs = getSP()

        return prefs.getString(name, defValue) ?: ""
    }

    fun removeOption(key: String) {
        val prefs = getSP()
        prefs.edit().remove(key).apply()
    }

    fun saveObj(key: String, obj: Any) {
        val toJson = GsonUtils.toJson(obj)
        toJson?.let { json ->
            getSP().edit()?.let {
                it.putString(key, json)
                it.apply()
            }
        }

    }

    /**
     * 保存配置文件
     *
     * @param ctx
     * @param key
     * @param value
     */
    fun savePrefs(key: String, value: Any) {

        val prefs = getSP()
            .edit()
        when (value) {
            is Boolean -> prefs.putBoolean(key, value)
            is Int -> prefs.putInt(key, value)
            is String -> prefs.putString(key, value)
            is Long -> prefs.putLong(key, value)
            is Float -> prefs.putFloat(key, value)
            else -> throw UnsupportedOperationException()
        }

        prefs.apply()
    }

    private fun getSP() = PreferenceManager
        .getDefaultSharedPreferences(ApplicationProxy.getApp())

}