package com.qing.androidkotlinutils.core.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.qing.androidkotlinutils.core.R
import kotlinx.android.synthetic.main.dialog_loading_little.*
/**
 *
 * 类说明: 通用loading 小
 *
 * @author qing
 * @time 2019-08-18 19:15
 */
class LittleLoadingDialog(activity: Activity, var desStr: String = "", var mCancelable: Boolean = false) : Dialog(activity, R.style.AppTheme_Dialog_Transparent_NoTitle_Dim) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading_little)
        tv_loading.text = desStr
        if (desStr.isEmpty()) {
            tv_loading.visibility = View.GONE
        }
        setCancelable(mCancelable)
        setCanceledOnTouchOutside(false)
    }

    override fun onBackPressed() {
        if (mCancelable) {
            super.onBackPressed()
        }
    }
}