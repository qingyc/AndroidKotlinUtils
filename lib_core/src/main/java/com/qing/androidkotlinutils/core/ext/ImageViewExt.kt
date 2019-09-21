package com.qing.androidkotlinutils.core.ext

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger

/**
 *
 * 类说明: imageView 扩展
 *
 * @author qing
 * @time 2019-08-21 11:49
 */
fun ImageView.setImageFromAsset(path: String) {
    val bitmap = try {
        val assetManager = ApplicationProxy.getApp().resources.assets
        val inputStream = assetManager.open(path)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        QLogger.e(e)
        null
    }
    bitmap?.let {
        setImageBitmap(bitmap)
    }
}