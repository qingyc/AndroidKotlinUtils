package com.qing.androidkotlinutils.core.mvp

import io.reactivex.disposables.Disposable

open class BasePresenter<M : IModel, V : IView> : IPresenter {

    var disposable: Disposable? = null
    var mModel: M? = null
    var mRootView: V? = null

    constructor(mModel: M?, mRootView: V?) {
        this.mModel = mModel
        this.mRootView = mRootView
    }

    override fun attachView() {

    }

    override fun detachView() {
        mModel?.onDestroy()
        disposable?.dispose()
    }

}