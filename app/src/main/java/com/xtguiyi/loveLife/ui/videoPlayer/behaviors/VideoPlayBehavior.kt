package com.xtguiyi.loveLife.ui.videoPlayer.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hjq.toast.Toaster
import kotlin.math.absoluteValue
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.common.view.CustomGSYVideoPlayerView
import kotlin.math.abs
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_AUTO_COMPLETE
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PAUSE

class VideoPlayBehavior(private val ctx: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<FrameLayout>(ctx, attrs) {

    private lateinit var topBarLayout: ConstraintLayout // topBar
    private lateinit var videoPlayerView: CustomGSYVideoPlayerView // 播放器
    private lateinit var viewPager2: ViewPager2 // viewPager2
    private lateinit var contentContainerLayout: LinearLayout // 内容区域

    private var totalOffset = 0 // 总偏移量
    private var topBarHeight = 0 // topBar高度
    private var initialVideoPlayerHeight = 0 // 播放器初始高度
    private var initialViewPagerHeight = 0 // viewPager初始高度
    private var canOffset = false


    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: FrameLayout,
        layoutDirection: Int
    ): Boolean {
        topBarLayout = parent.findViewById(R.id.topBar)
        videoPlayerView =  parent.findViewById(R.id.video_player)
        viewPager2 =  parent.findViewById(R.id.viewpager)
        contentContainerLayout =  parent.findViewById(R.id.content_container)
        if (topBarHeight == 0) topBarHeight = topBarLayout.measuredHeight
        if (initialVideoPlayerHeight == 0) initialVideoPlayerHeight = videoPlayerView.measuredHeight
        // 设置contentContainer高度为：屏幕高度 - topBar高度 - paddingTop - paddingBottom
        contentContainerLayout.layoutParams.height =
            parent.measuredHeight - topBarHeight - parent.paddingTop - parent.paddingBottom
        if(initialViewPagerHeight == 0) {
            parent.post {
                initialViewPagerHeight = viewPager2.height
            }
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
        canOffset =
            videoPlayerView.currentState == CURRENT_STATE_PAUSE || videoPlayerView.currentState == CURRENT_STATE_AUTO_COMPLETE
        Toaster.show("$canOffset--${videoPlayerView.currentState}")
        updateRvHeight()
        val (maxOffset, currentOffset) = if (canOffset) {
            initialVideoPlayerHeight - topBarHeight to totalOffset
        } else {
            0 to 0
        }
        offsetChangeListener?.invoke(maxOffset, currentOffset.absoluteValue)
        // 垂直方向的滚动以及暂停播放，播放完成才处理
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && canOffset
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
        // 这个方法用于，子child滚动之前，先让父元素去进行滚动
        // 向下滚动(上拉)， dy > 0
        // 向上滚动（下拉）， dy < 0
        // 变化范围：topBarHeight到videoPlayerHeight之间
        if (!canUpNestPreScroll(dy, target)) return
        val newOffset = totalOffset - dy
        // 实际消费dy
        val consumedY = if ((initialVideoPlayerHeight + newOffset) < topBarHeight) {
            (initialVideoPlayerHeight + totalOffset) - topBarHeight
        } else if ((initialVideoPlayerHeight + newOffset) > initialVideoPlayerHeight) {
            (initialVideoPlayerHeight + totalOffset) - initialVideoPlayerHeight
        } else {
            dy
        }
        totalOffset -= consumedY
        offsetChangeListener?.invoke(
            initialVideoPlayerHeight - topBarHeight,
            totalOffset.absoluteValue
        )
        consumed[1] = consumedY
        // 更新videoPlayer大小
        updateVideoPlayer(newOffset)
        // 更新content位置
        updateContent(consumedY)
        // 更新topBar透明度
        updateTopBar(abs(totalOffset))
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FrameLayout,
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
            val consumedY = if ((initialVideoPlayerHeight + newOffset) < topBarHeight) {
                (initialVideoPlayerHeight + totalOffset) - topBarHeight
            } else if ((initialVideoPlayerHeight + newOffset) > initialVideoPlayerHeight) {
                (initialVideoPlayerHeight + totalOffset) - initialVideoPlayerHeight
            } else {
                dyUnconsumed
            }
            totalOffset -= consumedY
            offsetChangeListener?.invoke(
                initialVideoPlayerHeight - topBarHeight,
                totalOffset.absoluteValue
            )
            consumed[1] = consumedY
            // 更新videoPlayer大小
            updateVideoPlayer(newOffset)
            // 更新content位置
            updateContent(consumedY)
            // 更新topBar透明度
            updateTopBar(abs(totalOffset))
        }
    }



    /**
     * 是否可以向上滚动,到达边界值将不能滚动
     * @param dy 垂直滚动距离
     * */
    private fun canUpNestPreScroll(dy: Int, child: View): Boolean {
        if (child is RecyclerView) {
            if (
                dy < 0
                && totalOffset == -(initialVideoPlayerHeight - topBarHeight)
                && child.canScrollVertically(-1)
            ) return false
        }
        return true
    }

    /**
     * 更新videoPlayer高度
     * @param offset 当前偏移大小
     * */
    private fun updateVideoPlayer(offset: Int) {
        val newHeight =
            (initialVideoPlayerHeight + offset).coerceIn(topBarHeight, initialVideoPlayerHeight)
        videoPlayerView.layoutParams.height = newHeight
        videoPlayerView.requestLayout()
    }

    /**
     * 更新Content偏移值
     * @param offset 当前偏移大小
     * */
    private fun updateContent(offset: Int) {
        contentContainerLayout.translationY -= offset.toFloat()
    }

    /**
     * 更新viewpage2内部的RecyclerView高度
     * */
    private fun updateRvHeight() {
        viewPager2.layoutParams.height =
            if (!canOffset) initialViewPagerHeight - (initialVideoPlayerHeight - topBarHeight) else initialViewPagerHeight
        viewPager2.requestLayout()

    }

    /**
     * 更新topBar样式
     * @param totalOffset 总偏移大小
     * */
    private fun updateTopBar(totalOffset: Int) {
        val meddle = (initialVideoPlayerHeight - topBarHeight) / 2
        topBarLayout.alpha = when {
            totalOffset <= meddle -> 0f
            else -> (totalOffset - meddle) * 1f / meddle
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
        canOffset = isPause
        updateRvHeight()
        val (maxOffset, currentOffset) = if (canOffset) {
            initialVideoPlayerHeight - topBarHeight to totalOffset
        } else {
            0 to 0
        }
        offsetChangeListener?.invoke(maxOffset, currentOffset.absoluteValue)
    }

    private var offsetChangeListener: ((Int, Int) -> Unit)? = null

    /**
     * 设置偏移度监听器
     * */
    fun setOnOffsetChange(mClickCall: (maxOffset: Int, offset: Int) -> Unit) {
        this.offsetChangeListener = mClickCall
    }
}