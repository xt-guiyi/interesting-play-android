package com.xtguiyi.loveLife.ui.videoPlayer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hjq.toast.Toaster
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_AUTO_COMPLETE
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PAUSE
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.common.view.CustomGSYVideoPlayer
import kotlin.math.abs

class NestParentLayout(private val ctx: Context, private val attrs: AttributeSet) :
    FrameLayout(ctx, attrs), NestedScrollingParent3 {

    private lateinit var mTopBar: ConstraintLayout // topBar
    private lateinit var mVideoPlayer: CustomGSYVideoPlayer // 播放器
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mContentContainer: LinearLayout // 内容区域

    private var totalOffset = 0 // 总偏移量
    private var topBarHeight = 0 // topBar高度
    private var videoPlayerHeight = 0 // 播放器初始高度
    private var viewPager2Height = 0 // 可滚动去区域初始高度
    private var canAbleScroll = false

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Toaster.show(mViewPager2.measuredHeight)
                if (viewPager2Height == 0) viewPager2Height = mViewPager2.measuredHeight
            }
        })
    }

    // 获取view实例
    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopBar = findViewById(R.id.topBar)
        mVideoPlayer = findViewById(R.id.video_player)
        mViewPager2 = findViewById(R.id.viewpager)
        mContentContainer = findViewById(R.id.content_container)
    }

    // 设置布局
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (topBarHeight == 0) topBarHeight = mTopBar.measuredHeight
        if (videoPlayerHeight == 0) videoPlayerHeight = mVideoPlayer.measuredHeight
        // 设置contentContainer高度为：屏幕高度 - topBar高度 - paddingTop - paddingBottom
        mContentContainer.layoutParams.height =
            measuredHeight - topBarHeight - paddingTop - paddingBottom

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
         canAbleScroll =
            mVideoPlayer.currentState == CURRENT_STATE_PAUSE || mVideoPlayer.currentState == CURRENT_STATE_AUTO_COMPLETE
        updateRvHeight(
            if(!canAbleScroll) viewPager2Height - (videoPlayerHeight - topBarHeight) else viewPager2Height
        )
        if(canAbleScroll) {
            mScrollHeightCall?.invoke(videoPlayerHeight - topBarHeight, totalOffset)

        }else {
            mScrollHeightCall?.invoke(0,0)
        }
        // 垂直方向的滚动以及暂停播放，播放完成才处理
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL && canAbleScroll
    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 这个方法用于，子child滚动之前，先让父元素去进行滚动
        // 向下滚动(上拉)， dy > 0
        // 向上滚动（下拉）， dy < 0
        // 变化范围：topBarHeight到videoPlayerHeight之间
        if (!canUpNestPreScroll(dy, target)) return
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
        mScrollHeightCall?.invoke(videoPlayerHeight - topBarHeight, totalOffset)
        consumed[1] = consumedY
        // 更新videoPlayer大小
        updateVideoPlayer(newOffset)
        // 更新content位置
        updateContent(consumedY)
        // 更新topBar透明度
        updateTopBar(abs(totalOffset))
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
        // 这个方法用于，子child滚动后，继续交给父元素去进行滚动
        // 这里是为了避免child滚动到顶部后，没办法依靠惯性继续让videoPlayer改变
        // 如果方向为向上

        if (dyUnconsumed < 0) {
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
            mScrollHeightCall?.invoke(videoPlayerHeight - topBarHeight, totalOffset)
            consumed[1] = consumedY
            // 更新videoPlayer大小
            updateVideoPlayer(newOffset)
            // 更新content位置
            updateContent(consumedY)
            // 更新topBar透明度
            updateTopBar(abs(totalOffset))
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
    ) {
    }


    /**
     * 是否可以向上滚动
     * @param dy 垂直滚动距离
     * */
    private fun canUpNestPreScroll(dy: Int, child: View): Boolean {
        if (child is RecyclerView) {
            if (
                dy < 0
                && totalOffset == -(videoPlayerHeight - topBarHeight)
                && child.canScrollVertically(-1)
            ) return false
        }
        return true
    }

    /**
     * 更新videoPlayer
     * @param offset 当前偏移大小
     * */
    private fun updateVideoPlayer(offset: Int) {
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
     * 更新viewpage2内部的RecyclerView高度
     * @param height 高度
     * */
    private fun updateRvHeight(height: Int) {
            mViewPager2.layoutParams.height  = height
        mViewPager2.requestLayout()

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

    /**
     * 重置状态
     * */
    fun resetStatus(isPause: Boolean) {
        // 更新videoPlayer大小
        updateVideoPlayer(0)
        // 更新content位置
        updateContent(totalOffset)
        // 更新topBar透明度
        updateTopBar(0)
        totalOffset = 0
        canAbleScroll = isPause
        updateRvHeight(
            if(!canAbleScroll) viewPager2Height - (videoPlayerHeight - topBarHeight) else viewPager2Height
        )
        if(canAbleScroll) {
            mScrollHeightCall?.invoke(videoPlayerHeight - topBarHeight, totalOffset)

        }else {
            mScrollHeightCall?.invoke(0,0)
        }
    }

    private var mScrollHeightCall: ((Int, Int) -> Unit)? = null

    public fun setOnScrollHeightChange(mClickCall: (maxOffset: Int, offset: Int) -> Unit) {
        this.mScrollHeightCall = mClickCall
    }
}
