package com.xtguiyi.loveLife.ui.videoPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.xtguiyi.loveLife.R

class MyNestLayout(val contex: Context, private val attributeSet: AttributeSet):LinearLayout(contex,attributeSet),NestedScrollingParent3 {
    private var originHeight = 735
    private var totalOffset = 0
    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        Log.i("MyNestLayout","onInterceptHoverEvent")
        return super.onInterceptHoverEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("MyNestLayout","onTouchEvent")
        return super.onTouchEvent(event)
    }


    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.i("MyNestLayout","onStartNestedScroll")
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.i("MyNestLayout","onNestedScrollAccepted")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.i("MyNestLayout","onStopNestedScroll")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.i("MyNestLayout","onNestedScroll1")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.i("MyNestLayout","onNestedScroll2")
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var min = originHeight / 2
        var max = originHeight

        val newOffset = totalOffset - dy
        val newHeight = (originHeight + newOffset).coerceIn(min, max)

        val videoPlayer = this.findViewById<NormalGSYVideoPlayer>(R.id.video_player)
        val lp = videoPlayer.layoutParams
        lp.height = newHeight
        videoPlayer.post {
            videoPlayer.layoutParams = lp

        }
        totalOffset -= dy
        consumed[1] = dy
        Log.i("MyNestLayout","onNestedPreScroll--dy:${dy}")
    }
}