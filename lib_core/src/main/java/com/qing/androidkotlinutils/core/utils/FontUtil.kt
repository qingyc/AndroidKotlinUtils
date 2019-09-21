package com.qing.androidkotlinutils.core.utils

import android.graphics.Typeface
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger

/**
 *
 * 类说明: 字体相关$
 *
 * @author qing
 * @time 2019-08-21 09:50
 */

object FontUtil {

    fun createFromAsset(path: String): Typeface? {
        return try {
            val assetManager = ApplicationProxy.getApp().assets
            Typeface.createFromAsset(assetManager, path)
        } catch (e: Exception) {
            QLogger.i(e)
            null
        }
    }

}