package com.xtguiyi.loveLife.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.xtguiyi.loveLife.R
import moe.codeest.enviews.ENDownloadView

/**
 * 自定义播放器
 * */
class CustomGSYVideoPlayer: StandardGSYVideoPlayer {

    constructor(context: Context, fullFlag: Boolean) : super(context,fullFlag) {}
    constructor(context: Context) : super(context) { }
    constructor(context: Context,  attrs: AttributeSet) : super(context,attrs) {}

    override fun getLayoutId(): Int {
        return R.layout.custom_video_player
    }

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.pause)
            }  else {
                imageView.setImageResource(R.drawable.play)
            }
        }
    }

    override fun getShrinkImageRes(): Int {
        return R.drawable.fullscreen
    }

    override fun getEnlargeImageRes(): Int {
        return R.drawable.fullscreen
    }

    override fun changeUiToPreparingShow() {
        setViewShowState(mTopContainer, VISIBLE)
        setViewShowState(mBottomContainer, VISIBLE)
        setViewShowState(mStartButton, VISIBLE)
        setViewShowState(mLoadingProgressBar, VISIBLE)
        setViewShowState(mThumbImageViewLayout, INVISIBLE)
        setViewShowState(mBottomProgressBar, INVISIBLE)
        setViewShowState(mLockScreen, GONE)

        if (mLoadingProgressBar is ENDownloadView) {
            val enDownloadView = mLoadingProgressBar as ENDownloadView
            if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }

    override fun changeUiToPlayingBufferingShow() {
        setViewShowState(mTopContainer, VISIBLE)
        setViewShowState(mBottomContainer, VISIBLE)
        setViewShowState(mStartButton, VISIBLE)
        setViewShowState(mLoadingProgressBar, VISIBLE)
        setViewShowState(mThumbImageViewLayout, INVISIBLE)
        setViewShowState(mBottomProgressBar, INVISIBLE)
        setViewShowState(mLockScreen, GONE)

        if (mLoadingProgressBar is ENDownloadView) {
            val enDownloadView = mLoadingProgressBar as ENDownloadView
            if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }

    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer {
        val gbPlayer = super.startWindowFullscreen(context, actionBar, statusBar)
        val customPlayer = gbPlayer as CustomGSYVideoPlayer
        // 设置全屏状态，底部操作栏适配沉浸式布局
        customPlayer.mBottomContainer.fitsSystemWindows = true
        return gbPlayer
    }
}