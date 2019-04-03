package com.demo.mvvm.general.core

import android.app.Activity
import android.os.Bundle
import android.support.annotation.CheckResult
import android.view.View
import com.airbnb.mvrx.BaseMvRxFragment
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class RxLifecycleMvRxFragment : BaseMvRxFragment(), LifecycleProvider<FragmentEvent> {
    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return this.lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(this.lifecycleSubject)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
    }

    override fun onStart() {
        super.onStart()
        this.lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onResume() {
        super.onResume()
        this.lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun onPause() {
        this.lifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        this.lifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    override fun onDestroyView() {
        this.lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.lifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }

    override fun onDetach() {
        this.lifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }
}