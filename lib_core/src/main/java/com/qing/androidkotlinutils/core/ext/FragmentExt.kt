package  com.qing.androidkotlinutils.core.ext

import androidx.fragment.app.Fragment
import com.qingyc.qlogger.QLogger


fun Fragment.showLoading() {
    try {
        activity?.showLoading()
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

fun Fragment.dismissLoading() {
    try {
        activity?.dismissLoading()
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

fun Fragment.showLittleLoading() {
    try {
        activity?.showLittleLoading()
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

fun Fragment.dismissLittleLoading() {
    try {
        activity?.dismissLittleLoading()
    } catch (e: Exception) {
        QLogger.e(e)
    }
}



fun Fragment.translucentStatus() {
    activity?.translucentStatus()
}

/**
 * 设置状态状态栏为light模式(图标深色)
 */
fun Fragment.setStatusBarLightMode(isLightMode: Boolean = true) {
    activity?.setStatusBarLightMode(isLightMode)

}

