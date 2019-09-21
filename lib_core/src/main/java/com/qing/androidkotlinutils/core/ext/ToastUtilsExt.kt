package  com.qing.androidkotlinutils.core.ext

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ToastUtils
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.utils.ResUtils

import kotlinx.android.synthetic.main.toast_little.view.*


object ToastUtilsExt {

    fun showMine(msg: String, drawable: Drawable? = null): View {
        val view = ToastUtils.showCustomShort(R.layout.toast_little)
        view?.tv_msg?.text = msg
        drawable?.let {
            view?.iv_icon?.setImageDrawable(drawable)
        }
        return view

    }

    fun showMine(msg: String, @DrawableRes drawableRes: Int): View {
        return ToastUtilsExt.showMine(msg, ResUtils.getDrawable(drawableRes))

    }

    fun showMine(@StringRes msgRes: Int, drawable: Drawable? = null): View {
        val view = ToastUtils.showCustomShort(R.layout.toast_little)
        view?.tv_msg?.text = ResUtils.getString(msgRes)
        drawable?.let {
            view?.iv_icon?.setImageDrawable(drawable)
        }
        return view

    }
}
