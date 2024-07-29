package com.xtguiyi.loveLife.ui.videoPlayer.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.xtguiyi.loveLife.R
import kotlin.math.abs

class VideoPlayBehavior( ctx: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<FrameLayout>(ctx, attrs) {

       private val topBarHeight = 150
       private var totalOffset = 0
       private var videoPlayerHeight = 0
       private lateinit var videoPlayer: NormalGSYVideoPlayer
       private lateinit var content: LinearLayout


    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: FrameLayout,
        layoutDirection: Int
    ): Boolean {
        if(!::videoPlayer.isInitialized) {
            videoPlayer = parent.findViewById(R.id.video_player)
            content = parent.findViewById(R.id.content)
            videoPlayerHeight = videoPlayer.measuredHeight
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: FrameLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FrameLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
       if(dy < 0 && (-totalOffset == videoPlayerHeight - topBarHeight)  && target is SmartRefreshLayout) {
            target.children.forEach {
                if(it is RecyclerView) {
                    Log.i("VideoPlayBehavior", "$it-${it.canScrollVertically(-1)}")
                    if(!target.canScrollVertically(-1)) return
                }
            }

       }
        val newOffset = totalOffset - dy
        val newHeight = MathUtils.clamp(videoPlayerHeight + newOffset, topBarHeight, videoPlayerHeight)
        videoPlayer.layoutParams.height = newHeight
        videoPlayer.requestLayout()

        var consumedY = dy
        if(dy > 0 && (videoPlayerHeight + newOffset) < topBarHeight) {
            consumedY =  (videoPlayerHeight + totalOffset) - topBarHeight
        }else if(dy < 0 && (videoPlayerHeight  + newOffset) > videoPlayerHeight) {
            consumedY =  (videoPlayerHeight + totalOffset) - videoPlayerHeight
        }
        totalOffset -= consumedY
        consumed[1] = consumedY
        updateContent(abs(newHeight).toFloat())
//        Log.i("VideoPlayBehavior","newHeight-${newHeight}-totalOffset:${totalOffset}-consumedY:${consumedY}-dy:${dy}")
    }

    private fun updateContent(offset: Float) {
        content.translationY = offset
    }


}