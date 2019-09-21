package com.qing.androidkotlinutils.core.helper

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
/**
 *
 * 类说明: view相关
 *
 * @author qing
 * @time 2019-09-21 16:08
 */
object ViewHelper {

    /**
     * layout to bitmap
     * @param activity
     * @param layoutId layoutId
     * @param initViewAction  用于layout的初始化
     */
    fun inflateLayoutAndGetBitmap(
        activity: Activity, @LayoutRes layoutId: Int,
        initViewAction: (inflateView: View) -> Unit
    ): Bitmap? {
        val inflate = LayoutInflater.from(activity).inflate(layoutId, null)
        inflate?.let { view ->
            initViewAction(view)
            if (view.isLayoutRequested) {
                val widthSpec: Int
                val heightSpec: Int
                val layoutParams = view.layoutParams
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
                view.measure(widthSpec, heightSpec)
                view.layout(
                    0, 0, view.measuredWidth,
                    view.measuredHeight
                )
            }

            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * QTIP: 2019-07-19 部分布局加载有耗时操作的添加一个延时
     * layout to bitmap
     * @param activity
     * @param layoutId layoutId
     * @param initViewAction  用于layout的初始化
     */
    fun inflateLayoutAndGetBitmap(
        activity: Activity, @LayoutRes layoutId: Int,
        initViewAction: (inflateView: View) -> Unit,
        callbackAction: (bitmap: Bitmap) -> Unit,
        delayTime: Long = 500
    ) {
        val inflate = LayoutInflater.from(activity).inflate(layoutId, null)
        inflate?.let { view ->
            initViewAction(view)
            if (view.isLayoutRequested) {
                val widthSpec: Int
                val heightSpec: Int
                val layoutParams = view.layoutParams
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
                view.measure(widthSpec, heightSpec)
                view.layout(
                    0, 0, view.measuredWidth,
                    view.measuredHeight
                )
            }

            //// QTIP: 2019-07-19 view 如果没有添加到window ,view.postDelayed不会调用?
            activity?.window?.decorView?.postDelayed({
                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                callbackAction.invoke(bitmap)
            }, delayTime)

        }
    }


}
