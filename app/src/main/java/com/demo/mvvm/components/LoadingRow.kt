package com.demo.mvvm.components

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.ModelView
import com.qmuiteam.qmui.widget.QMUIEmptyView

/**
 * 用于列表的末尾的 loading 圈圈，统一 UI
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : QMUIEmptyView(context, attrs, defStyleAttr) {

    init {
        show(true)
    }
}