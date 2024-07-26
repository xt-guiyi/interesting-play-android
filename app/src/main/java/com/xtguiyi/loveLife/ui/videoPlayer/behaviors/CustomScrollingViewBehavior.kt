package com.xtguiyi.loveLife.ui.videoPlayer.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import com.google.android.material.appbar.AppBarLayout
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.xtguiyi.loveLife.R

class CustomScrollingViewBehavior(val context: Context, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {
    private var mTouchSlop = 0
    private var originHeight = 735
    private var totalOffset = 0
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    init {
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
    }


    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.i("CustomScrollingViewBehavior","onInterceptTouchEvent")
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        Log.i("CustomScrollingViewBehavior","onStartNestedScroll")
        return super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }


    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        var min = 0
        var max = 0
        if(dy > 0) { // 向下滚动，手指上滑
            min = originHeight / 2
        }else if(dy < 0) { // 向上滚动，手指下滑
            max = originHeight
        }
          val newOffset = totalOffset - dy
          val videoPlayer = child.findViewById<NormalGSYVideoPlayer>(R.id.video_player)
          val lp = videoPlayer.layoutParams
          lp.height = originHeight + newOffset
//          videoPlayer.layoutParams = lp
          totalOffset -= dy
          consumed[1]= dy
        // totalScrollRange为可偏移距离，是视频的高度（height+margin），值735
        // topAndBottomOffset为已经偏移了多少距离，值0到-735
        // dy为用户尝试滚动的原始垂直像素数，正数为向下滚动，负数为向下滚动
        Log.i("CustomScrollingViewBehavior","totalScrollRange:${child.totalScrollRange}--topAndBottomOffset:${topAndBottomOffset}-dy:${dy}")
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
        Log.i("CustomScrollingViewBehavior","onStopNestedScroll")
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        Log.i("CustomScrollingViewBehavior","onMeasureChild")
        return super.onMeasureChild(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
        Log.i("CustomScrollingViewBehavior","onAttachedToLayoutParams")
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        abl: AppBarLayout,
        layoutDirection: Int
    ): Boolean {
        Log.i("CustomScrollingViewBehavior","onLayoutChild")
        return super.onLayoutChild(parent, abl, layoutDirection)
    }
}