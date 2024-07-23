package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.ActivityVideoPlayerBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.coroutines.launch


class VideoPlayerActivity :  GSYBaseActivityDetail<StandardGSYVideoPlayer>() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val viewModel: VideoBriefIntroductionViewModel by viewModels()
    private val id: Int by lazy {
        intent.getIntExtra("id", -1)
    }
    private lateinit var url : String

    init {
        GSYVideoType.enableMediaCodec()
        GSYVideoType.enableMediaCodecTexture()
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE)
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
//        CacheFactory.setCacheManager(ProxyCacheManager::class.java)
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        initView()
        initData()
        setContentView(binding.root)
    }

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
        return binding.videoPlayer;
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        //内置封面可参考SampleCoverVideo
//        val imageView = ImageView(this)
//        imageView.setImageResource(R.drawable.video_number)
//        loadCover(imageView, url)
        return GSYVideoOptionBuilder()
//            .setThumbImageView(imageView)
            .setUrl(url)
            .setCacheWithPlay(true)
            .setVideoTitle(" ")
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                   Log.i("GSYPlayer","播放失败")
                }

                override fun onPlayError(url: String?, vararg objects: Any?) {
                    Log.i("GSYPlayer","加载成功")
                }

            })
    }

    override fun clickForFullScreen() {
    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true;
    }


     private fun initView() {
        initTabLayoutAndViewPager()
    }

     private fun initData() {
        lifecycleScope.launch {
            viewModel.uiStateFlow.collect { uiState ->
                uiState.videoInfo?.let {
                    url = it.url
                    initVideoBuilderMode()
                }
            }
        }
    }

     fun bindingListener() {
        TODO("Not yet implemented")
    }

    private fun initTabLayoutAndViewPager() {
        // 初始化tabLayout
        val tabItems = listOf("简介")
        binding.tabs.isTabIndicatorFullWidth = false
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.tabs.setSelectedTabIndicatorColor(
            ResourcesCompat.getColor(resources,
                R.color.green_300,null))

        // 初始化viewPage
        val adapter = VideoPlayViewPageAdapter(this, tabItems.size,id)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabs,binding.viewpager) { tab, position ->
            tab.text = tabItems[position]
            // 去除长按提示，拦截长按事件
            tab.view.setOnLongClickListener { true }
        }.attach()
    }
}