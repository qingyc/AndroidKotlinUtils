package com.qing.androidkotlinutils.core.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger

/**
 *
 * 类说明:资源获取
 *
 * @author qing
 * @time 2019/4/9 13:41
 */
object ResUtils {
    fun getString(@StringRes resId: Int, vararg args: Any): String {
        //当我们调用 vararg-函数时，我们可以一个接一个地传参，例如 asList(1, 2, 3)，或者，
        //如果我们已经有一个数组并希望将其内容传给该函数，我们使用伸展（spread）操作符（在数组前面加 *）
        return ApplicationProxy.getApp().getString(resId, *args)
    }


    fun getString(@StringRes resId: Int): String {
        return ApplicationProxy.getApp().getString(resId)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(ApplicationProxy.getApp(), colorId)
    }

    /**
     * 根据drawable名字获取res
     */
    fun getDrawableResByName(resName: String): Int {
        return ApplicationProxy.getApp().resources.getIdentifier(resName, "drawable", ApplicationProxy.getApp().packageName)

    }

    /**
     * 根据drawable名字获取res
     */
    fun getDrawableByName(resName: String): Drawable? {
        return try {
            ContextCompat.getDrawable(ApplicationProxy.getApp(), getDrawableResByName(resName))
        } catch (e: Exception) {
            QLogger.e(e)
            null
        }
    }

    /**
     * get drawable
     */
    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(ApplicationProxy.getApp(), id)
    }


    fun readBitmapFromAssets(path: String): Bitmap? {
        return try {
            val assetManager = ApplicationProxy.getApp().resources.assets
            val inputStream = assetManager.open(path)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}



