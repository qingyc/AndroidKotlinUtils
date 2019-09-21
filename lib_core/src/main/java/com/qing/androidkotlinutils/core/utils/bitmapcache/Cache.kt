package com.qing.androidkotlinutils.core.utils.bitmapcache

import android.graphics.Bitmap

interface Cache {
    /**
     * 添加到缓存
     * @param k key
     * @param v value
     */
    fun put(k: String, v: Bitmap)

    /**
     * 获取缓存
     * @param k key
     */
    fun getBitmapCache(k: String): Bitmap?

    /**
     * 清空缓存
     */
    fun clearBitmapCache()
}