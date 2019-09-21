package com.qing.androidkotlinutils.core.base

import android.os.Bundle
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle3.components.support.RxFragment


open class BaseFragment : RxFragment() {

    var needInitBus = false
    // QTIP: 2019-06-05 子类在onCreate 中 super.onCreate(savedInstanceState) 前调用
    fun initBus() {
        needInitBus = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (needInitBus) {
            RxBus.get().register(this)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needInitBus) {
            try {
                RxBus.get().unregister(this)
            } catch (e: Exception) {
            }
        }
    }
}