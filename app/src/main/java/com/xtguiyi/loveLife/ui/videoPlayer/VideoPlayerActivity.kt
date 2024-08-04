package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.ActivityVideoPlayerBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager


class VideoPlayerActivity :  GSYBaseActivityDetail<StandardGSYVideoPlayer>() {
    // 这里我使用了两种方法实现，一种是自定义View，一种是使用协调者布局Behaviors
    private lateinit var binding: ActivityVideoPlayerBinding
    // private lateinit var binding: ActivityVideoPlayerTwoBinding
    private val viewModel: VideoBriefIntroductionViewModel by viewModels()
    private val id: Int by lazy {
        intent.getIntExtra("id", -1)
    }
    private lateinit var url : String

    init {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) // 使用Exo2内核
        GSYVideoType.enableMediaCodec() // 启动硬解码
        GSYVideoType.enableMediaCodecTexture()
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE) // 使用SurfaceView
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
//        CacheFactory.setCacheManager(ProxyCacheManager::class.java)
        //exo缓存模式，支持m3u8，只支持exo
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        // 修改状态栏
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        initView()
        initData()
        setContentView(binding.root)
    }

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer{
        return findViewById(R.id.video_player)
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        //内置封面可参考SampleCoverVideo
//        val imageView = ImageView(this)
//        imageView.setImageResource(R.drawable.video_number)
        return GSYVideoOptionBuilder()
//            .setThumbImageView(imageView)
            .setUrl(url)
            .setCacheWithPlay(true)
            .setIsTouchWiget(true)
            .setRotateViewAuto(true)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setStartAfterPrepared(true)
            .setNeedShowWifiTip(true)
    }

    override fun clickForFullScreen() {
    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true;
    }


     private fun initView() {
        initToolbar()
        initTabLayoutAndViewPager()
    }

     private fun initData() {
        lifecycleScope.launch {
            viewModel.videoInfoFlow.collect  { videoInfo ->
                videoInfo?.let {
                    // 设置样式
                    binding.videoPlayer.setBottomProgressBarDrawable(ResourcesCompat.getDrawable(resources, R.drawable.seek_bar_style,null))
                    binding.videoPlayer.setBottomShowProgressBarDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.seek_bar_style,null),
                        ResourcesCompat.getDrawable(resources, R.drawable.bilibil_vision,null)
                    )
                    binding.videoPlayer.setDialogProgressBar(ResourcesCompat.getDrawable(resources, R.drawable.seek_bar_style,null))
                    binding.videoPlayer.setDialogProgressColor(resources.getColor(R.color.green_300, null), resources.getColor(R.color.white, null))
                    binding.videoPlayer.setDialogVolumeProgressBar(ResourcesCompat.getDrawable(resources, R.drawable.progress_bar_style,null))
                    // 设置地址
                    url= it.url
//                    url = "https://privateimage-1306081565.cos.ap-shanghai.myqcloud.com/%5B%E5%8D%83%E5%A4%8F%E5%AD%97%E5%B9%95%E7%BB%84%5D%5B%E5%88%A9%E5%85%B9%E4%B8%8E%E9%9D%92%E9%B8%9F%5D%5BMovie%5D%5BBDRip_1080p%5D%5Bx264_AAC%5D%5BCHS%5D.mp4"
                    initVideoBuilderMode()
                    binding.videoPlayer.startPlayLogic()
                    true
                }?: false
            }
        }
    }

     fun bindingListener() {
        TODO("Not yet implemented")
    }

    private fun initToolbar() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
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