package com.qing.androidkotlinutils.core.base

import android.os.Bundle
import com.qing.androidkotlinutils.core.mvp.IPresenter


abstract class BaseMvpActivity<P : IPresenter> : BaseActivity() {

    private var mPresenter: IPresenter? = null
    abstract fun initPresenter(): P?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter?.attachView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPresenter?.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}