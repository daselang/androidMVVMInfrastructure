package com.demo.mvvm.components

import android.content.Context
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 用于请求发起后的加载等待，不用于 recycleView中 或者list 中，表现为统一的整个界面一个遮罩层
 */
class LoadingDialog constructor(context: Context) : QMUITipDialog.Builder(context) {
    init {
        setIconType(ICON_TYPE_LOADING)
        setTipWord("正在加载")
    }
}
