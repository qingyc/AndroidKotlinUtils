package com.qing.androidkotlinutils.core.helper

import android.content.Context
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy

/**
 *
 * 类说明: 判断键盘(输入法)是否添加到了系统
 *
 * @author qing
 * @time 2019/3/15 15:20
 */
fun isKeyboardAdd(keyboardPackageName: String): Boolean {
    val inputMethodManager =
        ApplicationProxy.getApp().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    //获取所有输入法
    val enabledInputMethodList = inputMethodManager.enabledInputMethodList
    for (inputMethodInfo in enabledInputMethodList) {
        if (keyboardPackageName == inputMethodInfo.packageName) {
            return true
        }
    }
    return false
}

fun isKeyboardisNowOpen(keyboardPackageName: String): Boolean {
    val defaultInputMethodInfo =
        Settings.Secure.getString(
            ApplicationProxy.getApp().contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        )
    val defaultKeyboardPackageName = defaultInputMethodInfo?.split("/")?.get(0)
    return defaultKeyboardPackageName == keyboardPackageName
}