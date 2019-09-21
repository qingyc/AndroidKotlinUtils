package com.qing.androidkotlinutils.core.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_common_has_cancel.*


/**
 * 类说明: 通用弹窗
 *
 * @author qing
 * @time 2018/10/24 15:04
 * @param hasCancelBtn 是否显示取消button(默认不显示)
 */
class CommonDialog(private val hasCancelBtn: Boolean = false) : BaseDialogFragment(), View.OnClickListener {


    private var mOnclickListener: OnclickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(if (hasCancelBtn) R.layout.dialog_common_has_cancel else R.layout.dialog_common, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        tv_confirm.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        arguments?.apply {
            val des = getString(KEY_DES)
            if (des.isNullOrEmpty()) {
                dismiss()
                return@apply
            }
            val confirmStr = getString(KEY_CONFIRM)
            tv_des.text = des
            if (!confirmStr.isNullOrEmpty()) {
                tv_confirm.text = confirmStr
            }
            val cancelStr = getString(KEY_CANCEL)
            if (!cancelStr.isNullOrEmpty()) {
                tv_cancel.text = cancelStr
            }
        } ?: dismiss()

    }

    fun setOnClickListener(clickListener: OnclickListener): CommonDialog {
        mOnclickListener = clickListener
        return this
    }

    override fun onClick(v: View?) {
        dismiss()
        when (v?.id) {
            R.id.tv_cancel -> {
                mOnclickListener?.onclick(false)
            }
            R.id.tv_confirm -> {
                mOnclickListener?.onclick(true)
            }
        }
    }

    interface OnclickListener {
        fun onclick(isConfirm: Boolean)
    }

    companion object {
        private const val KEY_DES = "KEY_DES"
        private const val KEY_CONFIRM = "KEY_CONFIRM"
        private const val KEY_CANCEL = "KEY_CANCEL"
        /**
         * 通用弹窗 有cancel btn
         */
        fun newInstanceWithCancelBtn(des: String?, confirmStr: String = "", cancelStr: String = ""): CommonDialog {
            val commonDialog = CommonDialog(true)
            val args = Bundle()
            args.putString(KEY_DES, des)
            args.putString(KEY_CONFIRM, confirmStr)
            args.putString(KEY_CANCEL, cancelStr)
            commonDialog.arguments = args
            return commonDialog
        }

        /**
         * 通用弹窗 无 cancel btn
         */
        fun newInstance(des: String?, confirmStr: String = "", cancelStr: String = ""): CommonDialog {
            val commonDialog = CommonDialog()
            val args = Bundle()
            args.putString(KEY_DES, des)
            args.putString(KEY_CONFIRM, confirmStr)
            args.putString(KEY_CANCEL, cancelStr)
            commonDialog.arguments = args
            return commonDialog
        }

    }


}