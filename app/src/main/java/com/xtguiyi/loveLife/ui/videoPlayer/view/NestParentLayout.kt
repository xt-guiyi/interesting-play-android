package com.xtguiyi.loveLife.ui.videoPlayer.view

import android.content.Context
import android.util.AttributeSet
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

class NestParentLayout(private val ctx: Context, private val attrs: AttributeSet) :
    FrameLayout(ctx, attrs), NestedScrollingParent3 {

    private lateinit var mTopBar: ConstraintLayout // topBar
    private lateinit var mVideoPlayer: StandardGSYVideoPlayer // 播放器
    private lateinit var mContentContainer: LinearLayout // 内容区域
    private var mChildViewId = -1 // 可滚动viewId, 这里因为Viewpage2里面使用的SmartRefreshLayout,导致onNestedPreScroll的target没办法拿到NestedScrollingChild3对应的实例
    private  val mChildView: View? by lazy {
        if(mChildViewId != -1) findViewById(mChildViewId) else null
    }

    private var totalOffset = 0 // 总偏移量
    private var topBarHeight = 0 // topBar高度
    private var videoPlayerHeight = 0 // 播放器初始高度

    // 获取AttributeSet设置的参数
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

    // 获取view实例
    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopBar = findViewById(R.id.topBar)
        mVideoPlayer = findViewById(R.id.video_player)
        mContentContainer = findViewById(R.id.content_container)
    }

    // 设置布局
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (topBarHeight == 0) topBarHeight = mTopBar.height
        if (videoPlayerHeight == 0) videoPlayerHeight = mVideoPlayer.height
        // 设置contentContainer高度为：屏幕高度 - topBar高度
        mContentContainer.layoutParams.height = measuredHeight - topBarHeight

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        // 垂直方向的滚动以及视频正在播放才处理
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL && !mVideoPlayer.getGSYVideoManager().isPlaying;
    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 向下滚动(上拉)， dy > 0
        // 向上滚动（下拉）， dy< 0
        // 变化范围：topBarHeight到videoPlayerHeight之间
        if(!canUpNestPreScroll(dy)) return
        val newOffset = totalOffset - dy
        // 实际消费dy
        val consumedY = if ((videoPlayerHeight + newOffset) < topBarHeight) {
            (videoPlayerHeight + totalOffset) - topBarHeight
        } else if ((videoPlayerHeight + newOffset) > videoPlayerHeight) {
            (videoPlayerHeight + totalOffset) - videoPlayerHeight
        } else {
            dy
        }
        totalOffset -= consumedY
        consumed[1] = consumedY
        // 更新videoPlayer大小
        updateVideoPlayer(newOffset)
        // 更新content位置
        updateContent(consumedY)
        // 更新topBar透明度
        updateTopBar(abs(totalOffset))
//        Log.i(
//            "NestParentLayout",
//            "onNestedPreScroll：dy:${dy}，consumedY:${consumedY}，totalOffset:${totalOffset}"
//        )
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
        // 如果是惯性滚动，且方向为向上
        // 这里是为了避免child滚动到顶部后，没办法依靠惯性继续让videoPlayer改变
        if(type == ViewCompat.TYPE_NON_TOUCH && dyUnconsumed < 0) {
            val newOffset = totalOffset - dyUnconsumed
            // 实际消费dy
            val consumedY = if ((videoPlayerHeight + newOffset) < topBarHeight) {
                (videoPlayerHeight + totalOffset) - topBarHeight
            } else if ((videoPlayerHeight + newOffset) > videoPlayerHeight) {
                (videoPlayerHeight + totalOffset) - videoPlayerHeight
            } else {
                dyUnconsumed
            }
            totalOffset -= consumedY

            consumed[1] = consumedY
            // 更新videoPlayer大小
            updateVideoPlayer(newOffset)
            // 更新content位置
            updateContent(consumedY)
            // 更新topBar透明度
            updateTopBar(abs(totalOffset))
//            Log.i(
//                "NestParentLayout",
//                "onNestedScroll：consumedY--${consumedY}，dyUnconsumed:${dyUnconsumed}，totalOffset:${totalOffset}"
//            )
        }
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {}
    override fun onStopNestedScroll(target: View, type: Int) {}
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {}


    /**
     * 是否可以向上滚动
     * @param dy 垂直滚动距离
     * */
    private fun canUpNestPreScroll(dy: Int): Boolean {
        if (mChildView is RecyclerView) {
            if (
                dy < 0
                && totalOffset == -(videoPlayerHeight - topBarHeight)
                && (mChildView as RecyclerView).canScrollVertically(-1)
            ) return false
        }
        return true
    }

    /**
     * 更新videoPlayer
     * @param offset 当前偏移大小
     * */
    fun updateVideoPlayer(offset: Int) {
        val newHeight = (videoPlayerHeight + offset).coerceIn(topBarHeight, videoPlayerHeight)
        mVideoPlayer.layoutParams.height = newHeight
        mVideoPlayer.requestLayout()
    }

    /**
     * 更新Content
     * @param offset 当前偏移大小
     * */
    private fun updateContent(offset: Int) {
        mContentContainer.translationY -= offset.toFloat()
    }

    /**
     * 更新topBar
     * @param totalOffset 总偏移大小
     * */
    private fun updateTopBar(totalOffset: Int) {
        val meddle = (videoPlayerHeight - topBarHeight) / 2
        if (totalOffset <= meddle) {
            mTopBar.alpha = 0f
        }
        if (totalOffset > meddle) {
            mTopBar.alpha = (totalOffset - meddle) * 1f / meddle
        }
    }
}
