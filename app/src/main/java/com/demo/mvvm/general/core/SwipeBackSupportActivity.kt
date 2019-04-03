package com.demo.mvvm.general.core

import me.yokeyword.fragmentation_swipeback.core.ISwipeBackActivity
import android.os.Bundle
import com.demo.mvvm.general.core.FragmentationSupportActivity
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.SwipeBackActivityDelegate


/**
 * You can also refer to {@link SwipeBackActivity} to implement YourSwipeBackActivity
 * (extends Activity and impl {@link me.yokeyword.fragmentation.ISupportActivity})
 * <p>
 * Created by YoKey on 16/4/19.
 */
abstract class SwipeBackSupportActivity : FragmentationSupportActivity(), ISwipeBackActivity {
    private val mDelegate by lazy { SwipeBackActivityDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mDelegate.swipeBackLayout
    }

    /**
     * 是否可滑动
     * @param enable
     */
    override fun setSwipeBackEnable(enable: Boolean) {
        mDelegate.setSwipeBackEnable(enable)
    }

    override fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel) {
        mDelegate.setEdgeLevel(edgeLevel)
    }

    override fun setEdgeLevel(widthPixel: Int) {
        mDelegate.setEdgeLevel(widthPixel)
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    override fun swipeBackPriority(): Boolean {
        return mDelegate.swipeBackPriority()
    }
}