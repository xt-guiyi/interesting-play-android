package com.xtguiyi.loveLife.ui.videoPlayer.behaviors

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.xtguiyi.loveLife.R

class ContentBehavior(private val ctx: Context, attr: AttributeSet): CoordinatorLayout.Behavior<LinearLayout>(ctx, attr) {
    private val topBarHeight = 150

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: LinearLayout,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        // 以下都可
//        val height = parent.height - topBarHeight
//        val childHeightMeasure = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY)
//        child.measure(parentWidthMeasureSpec,childHeightMeasure)
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, topBarHeight)
        return true
    }
}