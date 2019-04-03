package com.demo.mvvm.components

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.ModelView
import com.qmuiteam.qmui.widget.QMUIEmptyView

/**
 * 用于整个界面的空结果页面(常用于 list size = 0的情况)，统一 UI，覆盖整个 recycleView 单独使用
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class EmptyResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : QMUIEmptyView(context, attrs, defStyleAttr) {

    init {
        show(true)
    }
}