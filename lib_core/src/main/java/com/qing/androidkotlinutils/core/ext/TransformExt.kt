package  com.qing.androidkotlinutils.core.ext

import com.qing.androidkotlinutils.core.proxy.ApplicationProxy

/**
 *
 * 类说明:  通用
 *
 * @author qing
 * @time 2019-08-14 18:56
 */


fun Float.dp2Px(): Int {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this * density + .5f).toInt()
}

fun Int.dp2Px(): Int {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this * density + .5f).toInt()
}

fun Float.dp2PxFloat(): Float {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this * density + .5f)
}

fun Int.dp2PxFloat(): Float {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this * density + .5f)
}


fun Float.px2dp(): Int {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this / density).toInt()
}

fun Int.px2dp(): Int {
    val density = ApplicationProxy.getApp().resources.displayMetrics.density
    return (this / density).toInt()
}

