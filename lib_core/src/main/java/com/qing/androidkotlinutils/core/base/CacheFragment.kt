package com.qing.androidkotlinutils.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class CacheFragment : BaseFragment() {

    private var mContentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContentView ?: let {
            mContentView = onCreateContentView(inflater, container, savedInstanceState)
        }
        val parent = mContentView?.parent
        parent?.let {
            if (it is ViewGroup) {
                it.removeView(mContentView)
            }
        }
        return mContentView
    }


    fun getContentView(): View? {
        return mContentView
    }

    protected abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View


}