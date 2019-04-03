package com.demo.mvvm.others

import android.view.View
import android.view.ViewGroup

fun ViewGroup.asSequence(): Sequence<View> = (0..childCount).asSequence().map { getChildAt(it) }