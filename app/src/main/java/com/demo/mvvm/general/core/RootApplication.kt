package com.demo.mvvm.general.core

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.demo.mvvm.BuildConfig
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.vondear.rxtool.RxTool
import me.yokeyword.fragmentation.Fragmentation

class RootApplication : MultiDexApplication() {
    private lateinit var refWatcher: RefWatcher

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        refWatcher = setupLeakCanary()

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        Utils.init(this)
        RxTool.init(this)
        startUpFragmentStackBubble(this)
    }

    //安装内存检测
    //在 activity ,fragment 等组件的onDestroy中使用如下代码调用 watcher
    // RefWatcher refWatcher = RootApplication.getRefWatcher(getActivity());
    // refWatcher.watch(this)
    private fun setupLeakCanary(): RefWatcher {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED
        }
        return LeakCanary.install(this)
    }

    /**
     * 开启fragment 悬浮球栈视图功能,仅调试模式下启用
     */
    private fun startUpFragmentStackBubble(context: Context) {
        // 栈视图等功能，建议在Application里初始化
        if (BuildConfig.DEBUG) {
            Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .handleException {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                .install()
        }
    }

    companion object {
        fun getRefWatcher(context: Context): RefWatcher {
            val leakApplication = context.applicationContext as RootApplication
            return leakApplication.refWatcher
        }
    }
}