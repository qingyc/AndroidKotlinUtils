package  com.qing.androidkotlinutils.core.ext

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import com.blankj.utilcode.util.BarUtils
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.ui.dialog.LittleLoadingDialog
import com.qing.androidkotlinutils.core.ui.dialog.LoadingDialog
import com.qingyc.qlogger.QLogger



/*============================================= loading and dialog =============================================*/

var loadingDialog: LoadingDialog? = null
var littleLoadingDialog: LittleLoadingDialog? = null

/**
 * loading
 */
fun Activity.showLoading() {
    try {
        runOnUiThread {
            loadingDialog?.dismiss()
            loadingDialog = null
            loadingDialog = LoadingDialog(this)
            loadingDialog?.show()
        }
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

/**
 * dismiss loading
 */
fun Activity.dismissLoading() {
    try {
        runOnUiThread {
            loadingDialog?.dismiss()
        }
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

/**
 * 显示小的loading
 */
fun Activity.showLittleLoading(cancelable: Boolean = false, autoDismissTime: Long = -1L) {
    try {
        runOnUiThread {
            littleLoadingDialog?.dismiss()
            littleLoadingDialog = null
            littleLoadingDialog = LittleLoadingDialog(this, "", cancelable)
            littleLoadingDialog?.show()
        }
        if (autoDismissTime != -1L) {
            window?.decorView?.postDelayed({
                littleLoadingDialog?.dismiss()
            }, autoDismissTime)
        }
    } catch (e: Exception) {
        QLogger.e(e)
    }
}

fun Activity.dismissLittleLoading() {
    try {
        runOnUiThread {
            littleLoadingDialog?.dismiss()
        }
    } catch (e: Exception) {
        QLogger.e(e)
    }
}



/**
 * 判断当前Activity主题是否是透明
 */
fun Activity.getWindowIsTranslucent(): Boolean {
    val intArrayOf = intArrayOf(android.R.attr.windowIsTranslucent)
    val array = theme.obtainStyledAttributes(intArrayOf)
    array?.let {
        if (array.length() > 0) {
            val windowIsTranslucent = array.getBoolean(0, false)
            array.recycle()
            return windowIsTranslucent
        }
    }
    return false
}

/*============================================= status Bar =============================================*/

/**
 * 设置状态状态栏为light模式(图标深色)
 */
fun Activity.setStatusBarLightMode(isLightMode: Boolean = true) {
    BarUtils.setStatusBarLightMode(this, isLightMode)
}

/**
 * 透明状态栏
 */
fun Activity.translucentStatus() {
    val window = this.window
    window?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val winParams = window.attributes
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = winParams
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            val winParams = window.attributes
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = winParams
        }
    }
}

/**
 * 透明状态栏
 */
fun Activity.translucentStatusM() {
    val window = this.window
    window?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}


/**
 * 深色状态栏()
 * @receiver Activity
 */
@Deprecated("use setStatusBarLightMode(true)")
fun Activity.lightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

/**
 * 沉浸式状态栏
 * @receiver Activity
 */
fun Activity.immersiveStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }
}

/**
 * 透明状态栏(全透明)

 * @receiver Activity
 */

@Deprecated("")
fun Activity.translucentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = Color.TRANSPARENT
    }
}


/**
 * 透明状态栏(半透明)
 * @receiver Activity
 */
fun Activity.halfTranslucentStatusBar() {
    //**** 5.0 ****//
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    //**** 4.4 ****//
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}


/**
 * 设置状态栏颜色
 * @receiver Activity
 * @param color Int
 */
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    //**** 5.0 ****//
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = color
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

/**
 * 设置导航栏颜色
 * @receiver Activity
 * @param color Int
 */
fun Activity.setNavBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        BarUtils.setNavBarColor(this, color)
    }
}


/*============================================= Screen =============================================*/


/**
 *getPixelSize
 * @receiver Activity
 * @param id Int
 * @return Int
 */
fun Activity.getPixelSize(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}


/**
 * 设置竖屏
 * @receiver Activity
 */
fun Activity.setPortrait() {
    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

/**
 * 设置横屏
 * @receiver Activity
 */
fun Activity.setLandscape() {
    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}


/**
 * 全屏显示
 * @receiver Activity
 */
fun Activity.fullScreenDisplay() {
    // 隐藏标题栏
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    // 隐藏状态栏
    window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}


/*============================================= activity animation  =============================================*/


fun Activity.enterAlpha() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}


fun Activity.exitAlpha() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}


fun Activity.enterSlideTop() {
    overridePendingTransition(R.anim.slide_top_in, R.anim.slide_top_out)
}

fun Activity.enterNoAni() {
    overridePendingTransition(0, 0)
}


fun Activity.enterSlide() {
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}


fun Activity.exitSlide() {
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}


fun Activity.disableAnim() {
    overridePendingTransition(R.anim.anim_none, R.anim.anim_none)
}


/*============================================= start activity   =============================================*/

//fun Activity.startActivityKtx(cls: Class<*>) {
//    this.startActivity(Intent(this, cls))
//}

inline fun <reified T : Activity> Activity.startActivityKtx() {
    this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Activity.startActivityKtxWithParam(block: (intent: Intent) -> Any) {
    val intent = Intent(this, T::class.java)
    block.invoke(intent)
    this.startActivity(intent)
}

