package com.qing.androidkotlinutils.core.utils.bitmapcache

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.qingyc.qlogger.QLogger

/**
 *
 * @author qing
 * @time 21/05/2018 09:36
 */

object BitmapNativeCache : Cache {
    private val TAG = javaClass.name
    //android O 以后bitmap内存位于native
    private val nativeMemoryCacheLimit = Runtime.getRuntime().maxMemory()
    private val drawableCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(
        nativeMemoryCacheLimit.toInt()
    ) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            //https://github.com/square/picasso/issues/1693
            return value?.byteCount ?: 0
        }

        override fun entryRemoved(
            evicted: Boolean,
            key: String,
            oldValue: Bitmap,
            newValue: Bitmap?
        ) {
            super.entryRemoved(evicted, key, oldValue, newValue)
            QLogger.d(TAG, "entryRemoved === ")
        }

    }

    /**
     * 添加到缓存
     * @param k key
     * @param v value
     */
    override fun put(k: String, v: Bitmap) {
        QLogger.d(TAG, "BitmapLruCache ==== put $k")
        drawableCache.put(k, v)
    }

    /**
     * 获取缓存
     * @param k key
     */
    override fun getBitmapCache(k: String): Bitmap? {
        return drawableCache.get(k)
    }

    /**
     * 清空缓存
     */
    override fun clearBitmapCache() {
        QLogger.d(TAG, "BitmapLruCache ==== clear")
//        https://github.com/square/picasso/issues/1693
//        The reason getBitmapBytes() returns zero, is because the following if clause was added to the android Bitmap class getByteCount() method in api 26:
//        if (mRecycled) {  QLogger.d(TAG, "Called getByteCount() on a recycle()'d bitmap! " + "This is undefined behavior!"); return 0; }
        //qtip Caused by: java.lang.IllegalStateException: com.qing.androidkotlinutils.core.utils.bitmapcache.BitmapNativeCache$drawableCache$1.sizeOf() is reporting inconsistent results!
        drawableCache.evictAll()
    }

}