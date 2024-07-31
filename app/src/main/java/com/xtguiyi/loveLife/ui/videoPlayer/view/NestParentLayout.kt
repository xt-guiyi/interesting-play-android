package com.xtguiyi.loveLife.ui.videoPlayer.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.xtguiyi.loveLife.R
import kotlin.math.abs

class NestParentLayout(val ctx: Context, private val attrs: AttributeSet) :
    FrameLayout(ctx, attrs), NestedScrollingParent3 {

    private lateinit var mTopBar: ConstraintLayout
    private lateinit var mVideoPlayerContainer: FrameLayout
    private lateinit var mVideoPlay: StandardGSYVideoPlayer
    private lateinit var mContentContainer: LinearLayout
    private var mChildViewId = -1
    private  val mChildView: View? by lazy {
        if(mChildViewId != -1) findViewById(mChildViewId) else null
    }

    private var totalOffset = 0
    private var topBarHeight = 0
    private var videoPlayHeight = 0

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NestParentLayout,
            0, 0
        )

        try {
            mChildViewId = a.getResourceId(R.styleable.NestParentLayout_nestedChildId, -1)
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopBar = findViewById(R.id.topBar)
        mVideoPlayerContainer = findViewById(R.id.video_player_container)
        mVideoPlay = findViewById(R.id.video_player)
        mContentContainer = findViewById(R.id.content_container)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (topBarHeight == 0) topBarHeight = mTopBar.height
        if (videoPlayHeight == 0) videoPlayHeight = mVideoPlay.height
        // 设置contentContainer高度为：屏幕高度 - topBar高度
        mContentContainer.layoutParams.height = measuredHeight - topBarHeight

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.i("NestParentLayout","onStartNestedScroll")
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL && !mVideoPlay.getGSYVideoManager().isPlaying;
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 向下滚动(上拉)， dy > 0 ,
        // 向上滚动（下拉）， dy< 0
        if (mChildView is RecyclerView) {
//            AppBarLayout.Behavior
            Log.i("NestParentLayout","onNestedPreScroll---${(mChildView as RecyclerView).canScrollVertically(-1)}" )
            if (
                dy < 0
                && totalOffset == -(videoPlayHeight - topBarHeight)
                && (mChildView as RecyclerView).canScrollVertically(-1)
            ) return
        }
        val newOffset = totalOffset - dy
        // 新高度
        val newHeight = (videoPlayHeight + newOffset).coerceIn(topBarHeight, videoPlayHeight)
        mVideoPlay.layoutParams.height = newHeight
        mVideoPlay.requestLayout()
        // 实际消费dy
        val consumedY = if ((videoPlayHeight + newOffset) < topBarHeight) {
            (videoPlayHeight + totalOffset) - topBarHeight
        } else if ((videoPlayHeight + newOffset) > videoPlayHeight) {
            (videoPlayHeight + totalOffset) - videoPlayHeight
        } else {
            dy
        }
        totalOffset -= consumedY
        consumed[1] = consumedY
        // 更新content位置
        updateContentPosition(consumedY)
        // 更新topBar透明度
        updateTopBarAlpha(abs(totalOffset))
        Log.i(
            "NestParentLayout",
            "onNestedPreScroll：dy:${dy}，consumedY:${consumedY}，totalOffset:${totalOffset}"
        )
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {}

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.i(
            "NestParentLayout",
            "onStopNestedScroll"
        )
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
        Log.i("NestParentLayout","噜啦噜啦嘞--dyConsumed:${dyConsumed}--dyUnconsumed:${dyUnconsumed}")

    }


    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {}

    private fun updateContentPosition(offset: Int) {
        mContentContainer.translationY -= offset.toFloat()
    }

    private fun updateTopBarAlpha(totalOffset: Int) {
        val meddle = (videoPlayHeight - topBarHeight) / 2
        if (totalOffset <= meddle) {
            mTopBar.alpha = 0f
        }
        if (totalOffset > meddle) {
            mTopBar.alpha = (totalOffset - meddle) * 1f / meddle
        }
    }
}
