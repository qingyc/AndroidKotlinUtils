package com.qing.androidkotlinutils.core.helper

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import java.util.*
import kotlin.math.sqrt


/**
 *
 * 类说明: 通知栏颜色判断
 *
 * @author qing
 * @time 2019-09-21 11:44
 */
object NotificationHelper {

    /**
     * 通知栏是否是深色
     * @param context Context
     * @return Boolean
     */
    fun isDarkNotificationTheme(context: Context): Boolean {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context))
    }

    /**
     * 获取通知栏颜色
     *
     * @param context
     * @return
     */
    fun getNotificationColor(context: Context): Int {
        val builder = NotificationCompat.Builder(context)
        val notification = builder.build()
        val contentView = notification.contentView ?: return Color.WHITE
        val layoutId = contentView.layoutId
        val viewGroup = LayoutInflater.from(context)
            .inflate(layoutId, null, false) as ViewGroup
        return if (viewGroup.findViewById<View>(android.R.id.title) != null) {
            (viewGroup.findViewById<View>(android.R.id.title) as TextView).currentTextColor
        } else findColor(viewGroup)
    }

    /**
     * 检查颜色是否相识
     * @param checkColor Int
     * @param notificationBarColor Int
     * @return Boolean
     */
    private fun isSimilarColor(checkColor: Int, notificationBarColor: Int): Boolean {
        val simpleBaseColor = checkColor or -0x1000000
        val simpleColor = notificationBarColor or -0x1000000
        val baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor)
        val baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor)
        val baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor)
        val value =
            sqrt((baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue).toDouble())
        return value < 180.0
    }

    /**
     * 通知栏中查找颜色
     * @param viewGroupSource ViewGroup
     * @return Int
     */
    private fun findColor(viewGroupSource: ViewGroup): Int {
        var color = Color.TRANSPARENT
        val viewGroups = LinkedList<ViewGroup>()
        viewGroups.add(viewGroupSource)
        while (viewGroups.size > 0) {
            val viewGroup1 = viewGroups.first
            for (i in 0 until viewGroup1.childCount) {
                if (viewGroup1.getChildAt(i) is ViewGroup) {
                    viewGroups.add(viewGroup1.getChildAt(i) as ViewGroup)
                } else if (viewGroup1.getChildAt(i) is TextView) {
                    if ((viewGroup1.getChildAt(i) as TextView).currentTextColor != -1) {
                        color = (viewGroup1.getChildAt(i) as TextView).currentTextColor
                    }
                }
            }
            viewGroups.remove(viewGroup1)
        }
        return color
    }


}
