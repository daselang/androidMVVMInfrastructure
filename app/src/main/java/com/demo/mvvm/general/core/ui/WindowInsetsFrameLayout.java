package com.demo.mvvm.general.core.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Android Fragment 布局使用 fitsSystemWindows = true 无效解决方案
 * <p>
 * ref by https://www.jianshu.com/p/7bbce110a2fc
 *
 * @author cainiao
 */
public class WindowInsetsFrameLayout extends FrameLayout {
    public WindowInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    requestApplyInsets();
                } else {
                    requestFitSystemWindows();
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getChildAt(index).dispatchApplyWindowInsets(insets);
            } else {
                getChildAt(index).requestFitSystemWindows();
            }

        }
        return insets;
    }
}
