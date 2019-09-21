package com.qing.androidkotlinutils.core.base

import android.os.Bundle
import android.view.KeyEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {

    //点击返回不finish() 用于MainActivity 点击不finish
    open var openMoveTaskToBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewBeforeSuperOnCreate()
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        initLayout()
        initView()
        initData()
    }

    open fun initViewBeforeSuperOnCreate() {

    }

    abstract fun initLayout(): Int

    abstract fun initData()

    abstract fun initView()


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (openMoveTaskToBack) {
                moveTaskToBack(true)
                true
            } else {
                super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}