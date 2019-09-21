package com.qing.androidkotlinutils.core.utils.bitmapcache

import android.graphics.Bitmap
import android.os.Build

object BitmapCacheUtil {

    //qtip android 26 及以后 bitmap 内存为 native 内存
    private val cacheInNativeMemory = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    fun put(k: String, v: Bitmap) {
        if (cacheInNativeMemory) {
            BitmapNativeCache.put(k, v)
        } else {
            BitmapLruCache.put(k, v)
        }
    }

    /**
     * 获取缓存
     * @param k key
     */
    fun getBitmapCache(k: String): Bitmap? {
        return if (cacheInNativeMemory) {
            BitmapNativeCache.getBitmapCache(k)
        } else {
            BitmapLruCache.getBitmapCache(k)
        }
    }

    /**
     * 清空缓存
     */
    fun clearBitmapCache() {
        BitmapNativeCache.clearBitmapCache()
        BitmapLruCache.clearBitmapCache()
    }
}