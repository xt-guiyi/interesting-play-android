package com.example.lovelife.ui.home.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class CustomScrollingViewBehavior(context: Context?, attrs: AttributeSet?) : AppBarLayout.ScrollingViewBehavior(context,attrs) {
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val appBarLayout = dependency as AppBarLayout
        val scrollDistance = appBarLayout.top // 获取 AppBarLayout 的滚动距离

        // 计算目标 View 的滚动距离，并触发回调或发送事件
        onScrollDistanceChangedListener?.onScrollDistanceChanged(scrollDistance)
        Log.i("CustomScrollingViewBehavior", "Scroll Distance: $scrollDistance")
        return super.onDependentViewChanged(parent, child, dependency)
    }

    interface OnScrollDistanceChangedListener {
        fun onScrollDistanceChanged(scrollDistance: Int)
    }

    var onScrollDistanceChangedListener: OnScrollDistanceChangedListener? = null
}