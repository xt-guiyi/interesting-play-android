package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjq.toast.Toaster
import com.shuyu.aliplay.AliMediaPlayer
import com.shuyu.aliplay.AliPlayerManager
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.ActivityVideoPlayerBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


class VideoPlayerActivity :  GSYBaseActivityDetail<NormalGSYVideoPlayer>() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val viewModel: VideoBriefIntroductionViewModel by viewModels()
    private val id: Int by lazy {
        intent.getIntExtra("id", -1)
    }
    private lateinit var url : String

    init {
//        PlayerFactory.setPlayManager(AliPlayerManager::class.java) // 使用阿里云播放器
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) // 使用Exo2内核
        GSYVideoType.enableMediaCodec() // 启动硬解码
        GSYVideoType.enableMediaCodecTexture()
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE) // 使用SurfaceView
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // 状态栏字体为白色
        windowInsetsController.isAppearanceLightStatusBars = false

        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        initView()
        initData()
        setContentView(binding.root)
    }

    override fun getGSYVideoPlayer(): NormalGSYVideoPlayer? {
        return binding.videoPlayer;
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        //内置封面可参考SampleCoverVideo
//        val imageView = ImageView(this)
//        imageView.setImageResource(R.drawable.video_number)
//        loadCover(imageView, url)
        return GSYVideoOptionBuilder()
//            .setThumbImageView(imageView)
            .setVideoTitle("看")
            .setUrl(url)
            .setCacheWithPlay(true)
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
            viewModel.uiStateFlow.first  { uiState ->
                uiState.videoInfo?.let {
//                    url= it.url
                    url ="https://upos-sz-mirrorali.bilivideo.com/upgcxcode/00/20/306542000/306542000-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1721811708&gen=playurlv2&os=alibv&oi=17621919&trid=59aea925848144faa95f4ea4d1a6d7beh&mid=0&platform=html5&og=hw&upsig=4154b2ac00efc0f53be4d11a453fcf0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&bvc=vod&nettype=0&f=h_0_0&bw=53006&logo=80000000"
//                    url = "https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/20/08/964630820/964630820-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1721811197&gen=playurlv2&os=cosbv&oi=17621919&trid=80639885e99e429da070fa88b9d21eadh&mid=0&platform=html5&og=cos&upsig=4ff5c5ff9646b32cb351edac87c1220a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&bvc=vod&nettype=0&f=h_0_0&bw=51528&logo=80000000"
//                    url = "https://cn-gdfs-ct-01-07.bilivideo.com/upgcxcode/92/78/1623727892/1623727892-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1721801184&gen=playurlv2&os=bcache&oi=17621919&trid=00009f016e612e98458a9aefa28c5bf074c8h&mid=0&platform=html5&og=hw&upsig=c336c3fb23a1414e00a925b630f56438&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&cdnid=60907&bvc=vod&nettype=0&f=h_0_0&bw=60453&logo=80000000"
                    initVideoBuilderMode()
                    true
                }?: false
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