package com.demo.mvvm.general.core

import android.os.Bundle
import android.view.View
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackFragment
import me.yokeyword.fragmentation_swipeback.core.SwipeBackFragmentDelegate


abstract class SwipeBackSupportFragment : FragmentationSupportFragment(), ISwipeBackFragment {
    private val mDelegate by lazy { SwipeBackFragmentDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDelegate.onViewCreated(view, savedInstanceState)
    }

    override fun attachToSwipeBack(view: View): View {
        return mDelegate.attachToSwipeBack(view)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDelegate.onHiddenChanged(hidden)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mDelegate.swipeBackLayout
    }

    /**
     * 是否可滑动
     *
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
     * Set the offset of the parallax slip.
     */
    override fun setParallaxOffset(offset: Float) {
        if (offset in 0.0f..1.0f) {
            mDelegate.setParallaxOffset(offset)
        }
    }


    override fun onDestroyView() {
        mDelegate.onDestroyView()
        super.onDestroyView()
    }


}