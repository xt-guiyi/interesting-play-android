package com.xtguiyi.loveLife.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bytedance.danmaku.render.engine.DanmakuView
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.draw.text.TextData
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_SCROLL
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
    private lateinit var mDanmakuView: DanmakuView
    lateinit var mDanmakuController: DanmakuController
    private lateinit var mDanmakuData : MutableList<DanmakuData>


    override fun getLayoutId(): Int {
        return R.layout.custom_video_player
    }

    override fun init(context: Context?) {
        super.init(context)
        initDanmaku()
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


    // 开始全屏
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

    /**
     * 初始化弹幕库
     * */
    private fun initDanmaku() {
        mDanmakuView = findViewById(R.id.dan_ma_ku_view)
        mDanmakuController = mDanmakuView.controller
        mDanmakuData = buildDanmakuData()
        mDanmakuController.setData(mDanmakuData)

    }

    /**
     * 设置弹幕数据
     * */
    fun setDanmakuData(data: MutableList<DanmakuData>) {
        mDanmakuData = buildDanmakuData()
    }

    private fun buildDanmakuData(): MutableList<DanmakuData> {
        val contents = resources.getStringArray(R.array.danmaku_text_contents)
        val list = mutableListOf<DanmakuData>()
        for (i in 0..12) {
            list.add(TextData().apply {
                text = contents[i].toString()
                layerType = LAYER_TYPE_SCROLL
                showAtTime = i * 5200L +  (0..300L).random()
            })
        }
        return list
    }

    override fun clickStartIcon() {
        super.clickStartIcon()
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            mDanmakuController.start()
        } else if (mCurrentState == CURRENT_STATE_PAUSE) {
            mDanmakuController.pause()
        }
    }

    override fun onPrepared() {
        super.onPrepared()
        mDanmakuController.start()
    }

    override fun onVideoResume() {
        super.onVideoResume()
        mDanmakuController.start()
    }

    override fun onVideoPause() {
        super.onVideoPause()
        mDanmakuController.pause()
    }

    override fun onCompletion() {
        super.onCompletion()
        mDanmakuController.clear()
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
        mDanmakuController.clear()
    }

    override fun onSeekComplete() {
        super.onSeekComplete()
        mDanmakuController.clear()
        // TODO 弹幕库修改时间轴轨道无效
       if(isInPlayingState) mDanmakuController.start(currentPositionWhenPlaying)
    }

    override fun release() {
        super.release()
        mDanmakuController.stop()
    }
}