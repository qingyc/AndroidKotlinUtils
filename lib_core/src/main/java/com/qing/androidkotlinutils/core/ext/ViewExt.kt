package  com.qing.androidkotlinutils.core.ext

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 *  用于防止多次点击
 * @receiver View
 * @param delayTime Long
 * @param action Function0<Any>
 */
fun View.clickThrottleFirst(delayTime: Long = 600L, action: () -> Any) {
    this.isClickable = false
    action.invoke()
    this.postDelayed({
        try {
            this.isClickable = true
        } catch (e: Exception) {
        }
    }, delayTime)
}


/**
 * 延时恢复可点击
 * @receiver View
 * @param delayTime Long
 */
fun View.clickEnableDelay(delayTime: Long = 600L) {
    this.isClickable = false
    this.postDelayed({
        try {
            this.isClickable = true
        } catch (e: Exception) {
        }
    }, delayTime)
}


/**
 * to bitmap
 * @receiver View
 * @param bitmapConfig Config
 * @return Bitmap
 */
fun View.toBitmap(bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    check(ViewCompat.isLaidOut(this)) { "at least one layout since it was last attached to or detached from a window" }
    return when (this) {
        //竖直方向
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val outputBitmap = Bitmap.createBitmap(width, measuredHeight, bitmapConfig)
            val canvas = Canvas(outputBitmap)
            background?.apply {
                setBounds(0, 0, width, measuredHeight)
                draw(canvas)
            }
            this.draw(canvas)
            this.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
            )
            outputBitmap
        }

        else -> {
            val outputBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, bitmapConfig)
            val canvas = Canvas(outputBitmap)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)
            outputBitmap
        }
    }
}


/**
 * to bitmap
 *
 * @receiver View
 * @return Bitmap?
 */
@Deprecated("Deprecated")
fun View.toBitmap02(): Bitmap? {
    try {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            bitmap
        } else {
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
            isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(getDrawingCache(true))
            isDrawingCacheEnabled = false
            bitmap
        }
    } catch (e: Throwable) {
        //
    }
    return null
}


fun View.setBackgroundCompat(background: Drawable?) {
    ViewCompat.setBackground(this, background)
}


fun View.setBackgroundColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_ATOP) {
    if (color != -1) {
        background.setColorFilter(color, mode)
    }
}


fun View.measureView() {
    if (isLayoutRequested) {
        val widthSpec: Int
        val heightSpec: Int
        val layoutParams = layoutParams
        if (layoutParams != null) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(
                layoutParams.width,
                View.MeasureSpec.EXACTLY
            )
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                layoutParams.height,
                View.MeasureSpec.EXACTLY
            )
        } else {
            widthSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        }
        measure(widthSpec, heightSpec)
        layout(0, 0, measuredWidth, measuredHeight)
    }
}

/**
 * 从父中移除
 * @receiver View
 */
fun View.removeFromParent() {
    val p = parent
    if (p is ViewGroup) {
        p.removeView(this)
    }
}


/**
 * 设置宽高
 * @receiver View
 * @param newWidth Int?
 * @param newHeight Int?
 * @return View
 */
fun View.setWidthAndHeight(newWidth: Int? = null, newHeight: Int? = null): View {
    val lp = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    newHeight?.let {
        lp.height = it
    }
    newWidth?.let {
        lp.width = it
    }

    layoutParams = lp
    return this
}

/**
 * 获取view中心点
 * @receiver View
 * @return PointF
 */
fun View.getCenterPoint(): PointF {
    return PointF().apply {
        x = (left + right) / 2f
        y = (top + bottom) / 2f
    }
}


/**
 * set margin
 * @receiver View
 * @param l Int?
 * @param t Int?
 * @param r Int?
 * @param b Int?
 * @return View
 */
fun View.setMargin(l: Int? = null, t: Int? = null, r: Int? = null, b: Int? = null): View {
    val lp = layoutParams
    if (lp is ViewGroup.MarginLayoutParams) {
        l?.let {
            lp.leftMargin = it
        }
        t?.let {
            lp.topMargin = it
        }
        r?.let {
            lp.rightMargin = it
        }
        b?.let {
            lp.bottomMargin = it
        }
    }
    return this
}


fun View.setPaddingLTRB(padding: Int): View {
    setPadding(padding.dp2Px(), padding.dp2Px(), padding.dp2Px(), padding.dp2Px())
    return this

}

/**
 * 动态改变宽高
 * @receiver View
 * @param endW Int?
 * @param endH Int?
 * @param duration Long
 * @param callBack Function1<Float, Unit>?
 */
fun View.animateWH(
    endW: Int? = null,
    endH: Int? = null,
    duration: Long = 400,
    callBack: ((Float) -> Unit)? = null
) {
    post {
        val startW = width
        val startH = height
        val lp = layoutParams
        ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {

                val fraction = it.animatedFraction

                lp?.apply {
                    endH?.let {
                        height = startH + ((endH - startH) * fraction).toInt()
                    }
                    endW?.let {
                        height = startH + ((endW - startW) * fraction).toInt()
                    }
                }
                callBack?.invoke(fraction)
            }
            setDuration(duration)
            start()
        }
    }
}

/**
 * 点击监听 防止多次点击
 * @receiver View
 * @param action Function1<[@kotlin.ParameterName] View?, Unit>
 * @return View
 */
fun View.click(action: (view: View?) -> Unit): View {
    setOnClickListener {
        clickThrottleFirst {
            action(it)
        }
    }

    return this
}

/**
 * 长按监听
 * @receiver View
 * @param action Function1<[@kotlin.ParameterName] View?, Boolean>
 * @return View
 */
fun View.longClick(action: (view: View?) -> Boolean): View {
    setOnLongClickListener {
        action(it)
    }
    return this
}

