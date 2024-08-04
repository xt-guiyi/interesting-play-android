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
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.xtguiyi.loveLife.R
import kotlin.math.abs

class VideoPlayBehavior(private val ctx: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<FrameLayout>(ctx, attrs) {

    private lateinit var mTopBar: ConstraintLayout // topBar
    private lateinit var mVideoPlayer: StandardGSYVideoPlayer // 播放器
    private lateinit var mContentContainer: LinearLayout // 内容区域
    private var mChildView: View? = null

    private var totalOffset = 0 // 总偏移量
    private var topBarHeight = 0 // topBar高度
    private var videoPlayerHeight = 0 // 播放器初始高度
    private var defaultScrollViewHeight = 0 // 可滚动去区域初始高度

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: FrameLayout,
        layoutDirection: Int
    ): Boolean {
            mTopBar = parent.findViewById(R.id.topBar)
            mVideoPlayer = parent.findViewById(R.id.video_player)
            mContentContainer = parent.findViewById(R.id.content)
            mChildView = parent.findViewById(R.id.rv) // 直接通过id获取
            if(videoPlayerHeight  == 0) videoPlayerHeight = mVideoPlayer.height
            if(topBarHeight == 0) topBarHeight = mTopBar.height

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
        if (defaultScrollViewHeight == 0) defaultScrollViewHeight = target.height
        updateScrollableHeight(
            target,
            if (mVideoPlayer.gsyVideoManager.isPlaying) defaultScrollViewHeight - (videoPlayerHeight - topBarHeight) else defaultScrollViewHeight
        )
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && !mVideoPlayer.getGSYVideoManager().isPlaying;
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
//        Log.i("VideoPlayBehavior","newHeight-${newHeight}-totalOffset:${totalOffset}-consumedY:${consumedY}-dy:${dy}")
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

    /**
     * 是否可以向上滚动
     * @param dy 垂直滚动距离
     * */
    private fun canUpNestPreScroll(dy: Int): Boolean {
        Log.i("canUpNestPreScroll", mChildView.toString())
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
     * 更新可滚动view高度
     * @param child 可滚动view
     * @param height 高度
     * */
    private fun updateScrollableHeight(child: View, height: Int) {
        if(child is RecyclerView) {
            child.layoutParams.height = height
            child.requestLayout()
        }
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