package com.qing.androidkotlinutils.core.proxy

import android.app.Application

object ApplicationProxy {
    private var sApp: Application? = null
    fun init(app: Application) {
        sApp = app
    }
    fun getApp(): Application {
        if (sApp == null) {
            throw IllegalStateException("ApplicationProxy.init() has not been called !!!!!!!!!!!!! ")
        }
        return sApp!!
    }
}