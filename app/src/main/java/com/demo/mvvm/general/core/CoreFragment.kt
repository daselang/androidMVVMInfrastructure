package com.demo.mvvm.general.core

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.MvRx
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.demo.mvvm.general.eventbus.EventBusUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class CoreFragment : SwipeBackSupportFragment() {
    /**
     * the same instance as mRecycleView
     */
    lateinit var recycleViewInstance: EpoxyRecyclerView

    /**
     * the injected real epoxy controller instance
     */
    protected val epoxyControllerInstance by lazy { fetchEpoxyController() }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (needUseARouterInjection()) {
            ARouter.getInstance().inject(this)
        }
    }

    @CallSuper
    override fun invalidate() {
        recycleViewInstance.requestModelBuild()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        //尽量比较晚注册和响应 eventBus 事件，界面 UI都确保生成好
        if (needRegisterEventBus()) {
            EventBusUtils.register(this)
        }
    }

    @CallSuper
    override fun onDestroy() {
        if (needRegisterEventBus()) {
            EventBusUtils.unregister(this)
        }
        super.onDestroy()
        try {
            //启用内存泄露检测
            val watcher = RootApplication.getRefWatcher(activity!!)
            watcher.watch(this)
        } catch (e: Exception) {

        }
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultHorizontalAnimator()
    }

    abstract fun fetchEpoxyController(): EpoxyController


    /**
     * 是否需要注册 eventBus 的开关，由子类复写此方法，默认false 不注册
     */
    protected open fun needRegisterEventBus(): Boolean = false

    /**
     * 是否需要使用 ARouter 的自动参数注入
     * 默认为 true，即启用
     */
    protected open fun needUseARouterInjection(): Boolean = true

    /**
     * 配置统一的 fragment 的退出策略
     *
     * 当只剩下当前一个 activity 时且这个 activity 实例为当前 fragment 所在的 activity，
     * 并且当前 fragment 为此 activity 管理剩下的唯一一个栈fragment对象时，finish all activities 即程序终止既出;
     * 当当前 fragment 为activity 中唯一一个 fragment 时 finish 所在的 activity，即表现为弹出到上一个 activity;
     * 否则仅弹出当前 fragment,回退到上一个 fragment
     *
     * 改变策略时请 overwrite 本方法，想使之失效请 overwrite并且函数体置空。
     *
     * @see SwipeBackSupportActivity.swipeBackPriority
     * @see onStart
     */
    protected open fun launchUniversalPopUpStrategy() {
        val fragmentManager = activity?.supportFragmentManager
        val isOnlyOneActivityRunning = ActivityUtils.getActivityList().size <= 1
        val isTopActivity = ActivityUtils.getTopActivity() == activity
        val isFirstFragment =
            fragmentManager?.let {
                val fragments = FragmentUtils.getFragments(it)
                fragments.size <= 1 && (fragments.getOrNull(fragments.lastIndex)?.equals(this) ?: false)
            } ?: false
        if (isOnlyOneActivityRunning && isTopActivity && isFirstFragment) {
            showQuitAppDialog()
        } else if (isFirstFragment) {
            activity?.let {
                ActivityUtils.finishActivity(it, true)
            }
        } else {
            pop()
        }
    }


    /**
     * 使用 rxJava 在 UI主线程异步执行定义的具体操作，绑定生命周期了，有效阻止内存泄露
     */
    protected fun executeActions(actions: () -> Unit) {
        Completable.fromAction(actions)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .bindUntilEvent(this, FragmentEvent.DESTROY)
            .subscribe()
    }


    /**
     * 跳转到下一个fragment,仅支持 ISupportFragment 的 router,
     * <br/>如果targetRouter指定的目标对象不是ISupportFragment，则不会跳转
     * @param targetRouter 指代要跳转的 目标 fragment 的路由 url，使用的是阿里的 ARouter 框架
     * @param params 传递给下一个 fragment的信息对象，可为 null,在 target Fragment 中使用 args()函数获取
     *
     */
    protected fun popToNextFragment(targetRouter: String, params: Any? = null) {
        executeActions {
            val fragment = ARouter.getInstance().build(targetRouter).withObject(MvRx.KEY_ARG, params).navigation()
            if (fragment is ISupportFragment) {
                start(fragment)
            }
        }
    }

    protected fun showQuitAppDialog() {
        QMUIDialog.MessageDialogBuilder(activity)
            .setTitle("关闭程序")
            .setMessage("确定要退出吗？")
            .addAction("取消") { dialog, _ -> dialog.dismiss() }
            .addAction(
                0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE
            ) { dialog, _ ->
                dialog.dismiss()
                ActivityUtils.finishAllActivities(true)
            }
            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }
}