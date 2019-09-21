package com.qing.androidkotlinutils.core.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import com.qing.androidkotlinutils.core.R

/**
 *
 * 类说明: 通用loading
 *
 * @author qing
 * @time 2019-08-18 19:15
 */
class LoadingDialog(activity : Activity) :Dialog(activity){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCanceledOnTouchOutside(false)
    }
}