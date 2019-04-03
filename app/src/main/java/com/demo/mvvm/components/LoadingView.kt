package com.demo.mvvm.components

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.ModelView
import com.qmuiteam.qmui.widget.QMUIEmptyView

/**
 * 用于整个界面的 loading 圈圈，统一 UI，覆盖整个 recycleView 单独使用
 *
 * 一般用于列表不分页 一次性加载所有数据的情况，和整个界面加载的情况
 *
 * 列表分页加载末尾的 loading status view 一般采用 LoadingRow
 * @see LoadingRow
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : QMUIEmptyView(context, attrs, defStyleAttr) {

    init {
        show(true)
    }
}