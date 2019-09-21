package com.qing.androidkotlinutils

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger

object AkuBase {

    /**
     * Application onCreate() 中 初始化 AkuBase.init(this, BuildConfig.DEBUG)
     */
    fun init(app: Application, debug: Boolean = false) {

        ApplicationProxy.init(app)
        //blankj
        Utils.init(app)
        //log 开关
        val settings = QLogger.init(debug)
        settings.setMethodCount(4)
        QLogger.i("DEBUG OPEN ==== $debug")
    }
}