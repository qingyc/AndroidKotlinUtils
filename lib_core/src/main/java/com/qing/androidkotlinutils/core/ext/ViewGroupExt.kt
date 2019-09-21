package com.qing.androidkotlinutils.core.ext

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

/**
 *
 * 类说明: ViewGroup扩展
 *
 * @author qing
 * @time 2019-08-21 18:47
 */
fun ViewGroup.removeAllChildBut(viewNotRemove: View) {
    for (child in children) {
        if (child != viewNotRemove) {
            child.removeFromParent()
        }
    }
}

fun ViewGroup.removeAllChildBut(vararg viewsNotRemove: View) {
    for (child in children) {
        if (!viewsNotRemove.contains(child)) {
            child.removeFromParent()
        }
    }
}