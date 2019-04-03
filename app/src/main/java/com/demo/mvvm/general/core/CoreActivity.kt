package com.demo.mvvm.general.core

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import com.airbnb.mvrx.MvRx
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.demo.mvvm.general.eventbus.EventBusUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


abstract class CoreActivity : SwipeBackSupportActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (makeStatusBarTranslucent()) {
            QMUIStatusBarHelper.translucent(this)
        }
        if (needUseARouterInjection()) {
            ARouter.getInstance().inject(this)
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        //尽量比较晚注册和响应 eventBus 事件，界面 UI都确保生成好
        if (needRegisterEventBus()) {
            //onStart 回调会多次调用所以在 register里面做了防重复注册
            EventBusUtils.register(this)
        }
    }

    @CallSuper
    override fun onDestroy() {
        if (needRegisterEventBus()) {
            EventBusUtils.unregister(this)
        }
        super.onDestroy()
        //启用内存泄露检测
        val refWatcher = RootApplication.getRefWatcher(this)
        refWatcher.watch(this)
    }


    /**
     * 屏幕旋转等 UI 改变时不销毁对象实例，而是会回调此方法
     * android:configChanges="keyboardHidden|orientation|screenSize"
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultHorizontalAnimator()
    }

    /**
     * overwrite 后，当当前活动的 activity 只剩当前实例时，不支持 activity 本身的滑动退出，否则可以滑动 finish 当前 activity
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    override fun swipeBackPriority(): Boolean {
        if (ActivityUtils.getActivityList().size <= 1 && ActivityUtils.getTopActivity() == this) {
            return false
        }
        return super.swipeBackPriority()
    }

    override fun onBackPressedSupport() {
        if (ActivityUtils.getActivityList().size <= 1 && ActivityUtils.getTopActivity() == this && FragmentUtils.getAllFragments(
                this.supportFragmentManager
            ).size <= 1
        ) {
            showQuitAppDialog()
        } else {
            super.onBackPressedSupport()
        }
    }

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
     * 是否启用沉浸式状态栏控制开关，true 启用，false 不启用，默认为 false，即不启用
     */
    protected open fun makeStatusBarTranslucent(): Boolean = true


    /**
     * 使用 rxJava 在 UI主线程异步执行定义的具体操作，绑定生命周期了，有效阻止内存泄露
     */
    protected fun executeActions(actions: () -> Unit) {
        Completable.fromAction(actions)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .bindUntilEvent(this, ActivityEvent.DESTROY)
            .subscribe()
    }

    /**
     * 加载初始化的第一个可显示的 fragment view,<br/>
     * 仅支持 ISupportFragment 接口的fragmentRouter才加载
     * @param fragmentRouter 指代要加载的fragment
     * @param params 传递给目标对象的 parcel 参数集合
     */
   protected fun loadRootFragmentView(fragmentRouter: String, params: Parcelable? = null) {
        executeActions {
            val rootView = ARouter.getInstance().build(fragmentRouter).withParcelable(MvRx.KEY_ARG, params).navigation()
            if (rootView is ISupportFragment) {
                loadRootFragment(toFragment = rootView, allowAnimation = true)
            }
        }
    }

    protected fun showQuitAppDialog() {
        QMUIDialog.MessageDialogBuilder(this)
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
