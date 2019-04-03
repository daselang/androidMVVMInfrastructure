package com.demo.mvvm.general.core

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.demo.mvvm.BuildConfig
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class CoreViewModel<S : MvRxState>(initialState: S) :
    BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG) {
    /**
     * 在新线程上执行操作
     */
    protected fun executeActions(action: () -> Unit): Disposable =
        Completable
            .fromAction(action)
            .subscribeOn(Schedulers.io())
            .subscribe().disposeOnClear()
}