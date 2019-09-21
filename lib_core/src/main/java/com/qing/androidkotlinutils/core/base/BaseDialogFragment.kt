package com.qing.androidkotlinutils.core.base

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.qing.androidkotlinutils.core.R

open class BaseDialogFragment : DialogFragment() {

    //用于子类控制
    var needDismissActionFlag = true
    var mActivity: FragmentActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog_Transparent_NoTitle)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // QTIP: 2019/2/19 dialogFragment 设置动画不能在onCreate 时设置,因为onCreate时 dialog?.window?为null
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_window_anim
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
        // QTIP: 2019-07-07 dismiss不一定会调用
        //消失触发动作
        //dismissAction?.invoke()
    }

    override fun onDetach() {
        super.onDetach()
        //消失触发动作
        if (needDismissActionFlag) {
            dismissAction?.invoke()
        }

    }

    override fun onPause() {
        super.onPause()
        //onResume时不再次弹出动画
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_window_anim_null
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val displayMetrics = DisplayMetrics()
            val metrics = activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            metrics?.let {
                val widthPixels = displayMetrics.widthPixels
                //val heightPixels = displayMetrics.heightPixels
                dialog.window?.let {
                    //// QTIP: 2019/2/19 解决dialogFragment宽度异常问题
                    it.setLayout(widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
                    /*//qtip 延伸到状态栏 等效  <item name="android:windowTranslucentStatus">true</item>
                    val decorView = it.decorView
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        it.statusBarColor = Color.TRANSPARENT
                    }*/
                }
            }
        }
    }


    fun show(manager: FragmentManager) {
        show(manager, "")
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (activity != null && activity!!.isFinishing) {
                return
            }
            super.show(manager, "$tag")
        } catch (e: Exception) {
            //
        }
    }


    private var dismissAction: (() -> Unit)? = null
    fun showWithDismissAction(manager: FragmentManager, action: (() -> Unit)?) {
        dismissAction = action
        show(manager, "")
    }

    fun showWithDismissAction(manager: FragmentManager, tag: String?, action: (() -> Unit)?) {
        dismissAction = action
        show(manager, tag)
    }


}