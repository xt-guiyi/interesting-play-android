package com.example.loveLife.ui.home.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.loveLife.R
import com.example.loveLife.utils.DisplayUtil
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class CustomScrollingViewBehavior(val context: Context, attrs: AttributeSet?) :
    AppBarLayout.ScrollingViewBehavior(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        val scrollDistance = abs(appBarLayout.top) // 获取 AppBarLayout 的滚动距离
        val maxScrollDistance = DisplayUtil.dip2px(context, 50f) // 最大滚动距离，对应透明度为 1
        val homeTitleBar = appBarLayout.findViewById<ConstraintLayout>(R.id.included_header)
        // 计算透明度
        val alpha = if (scrollDistance <= 0) {
            1f
        } else if (scrollDistance >= maxScrollDistance) {
            0f
        } else {
            1 - (scrollDistance.toFloat() / maxScrollDistance)
        }
        homeTitleBar.alpha = alpha
        Log.i("CustomScrollingViewBehavior", "alpha: $alpha")
        return super.onDependentViewChanged(parent, child, dependency)
    }
}