package com.qing.androidkotlinutils.core.ext

import android.net.Uri
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import java.io.InputStream
import java.net.URL


fun Uri.toInputStream(): InputStream? {
    val uri = this
    //网络
    return if (uri.scheme?.startsWith("http") == true || uri.scheme?.startsWith("https") == true) {
        URL(uri.toString()).openStream()
    } else {
        val path = uri.path ?: ""

        //asset
        if (path.startsWith("/android_asset/")) {
            ApplicationProxy.getApp().assets.open(path.substring("/android_asset/".length))
        } else {
            ApplicationProxy.getApp().contentResolver?.openInputStream(uri)
        }
    }
}