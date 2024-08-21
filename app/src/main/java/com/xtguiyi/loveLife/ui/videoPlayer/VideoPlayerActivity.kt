package com.xtguiyi.loveLife.ui.videoPlayer

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bytedance.danmaku.render.engine.render.draw.text.TextData
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_BOTTOM_CENTER
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_SCROLL
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_TOP_CENTER
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjq.toast.Toaster
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.base.BaseActivity
import com.xtguiyi.loveLife.databinding.ActivityVideoPlayerBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.BarrageDialogFragment
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.BarrageDialogFragment.BarrageInfo
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.BriefIntroductionViewModel
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoPlayerViewModel
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager
import kotlin.math.roundToInt


class VideoPlayerActivity : BaseActivity(),
    BarrageDialogFragment.OnBarrageListener {

    // 这里我使用了两种方法实现，一种是自定义View，一种是使用协调者布局Behaviors
    private lateinit var binding: ActivityVideoPlayerBinding
    // private late init var binding: ActivityVideoPlayerTwoBinding

    private val videoPlayerViewModel: VideoPlayerViewModel by viewModels()
    private val briefIntroductionViewModel: BriefIntroductionViewModel by viewModels()
    private val id: Int by lazy {
        intent.getIntExtra("id", -1)
    }
    private lateinit var orientationUtils: OrientationUtils
    private var isPlay: Boolean = false
    private val gsyVideoOption = GSYVideoOptionBuilder()
    private var barrageInfo = BarrageInfo("","默认","滚动","#FFFFFFFF")
    // TODO 直接引用，试试会不会内存泄露
    private val barrageDialog: BarrageDialogFragment by lazy{
        BarrageDialogFragment(barrageInfo)
    }
    private val fragmentManagerListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            if(f is BarrageDialogFragment){
                binding.barrageInput.text = resources.getString(R.string.barrage_input_text_1)
            }
        }
    }

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
        // 修改状态栏, 适配沉浸式布局
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            lifecycleScope.launch { videoPlayerViewModel.setNavHeight(systemBars.bottom) }
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentManagerListener, true)
        initView()
        initData()
        bindingListener()
        setContentView(binding.root)
    }


    override fun initView() {
        initStatus()
        initTabLayoutAndViewPager()
        initPlayer()
    }

    override fun initData() {
        lifecycleScope.launch {
            briefIntroductionViewModel.videoInfoFlow.collect { videoInfo ->
                videoInfo?.let {
                    // 设置地址
                    gsyVideoOption
                        .setVideoTitle("测试视频")
                        .setUrl(it.url)
//                        .setUrl("https://privateimage-1306081565.cos.ap-shanghai.myqcloud.com/%5B%E5%8D%83%E5%A4%8F%E5%AD%97%E5%B9%95%E7%BB%84%5D%5B%E5%88%A9%E5%85%B9%E4%B8%8E%E9%9D%92%E9%B8%9F%5D%5BMovie%5D%5BBDRip_1080p%5D%5Bx264_AAC%5D%5BCHS%5D.mp4")
                        .build(binding.videoPlayer)
                    binding.videoTitle.text = "测试视频"
                    binding.videoPlayer.startPlayLogic()
                    true
                } ?: false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindingListener() {
        binding.backMain.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.barrageInput.setOnClickListener {
            binding.barrageInput.text = resources.getString(R.string.barrage_input_text_2)
            barrageDialog.show(supportFragmentManager, BarrageDialogFragment.TAG)
        }
    }

    private fun initStatus() {
        val calculatorHeight = calculateVideoViewHeight()
        binding.videoPlayerContainer.layoutParams.height = calculatorHeight.toInt()
        binding.videoPlayerContainer.requestLayout()
        binding.contentContainer.translationY = calculatorHeight.toFloat()
    }

    private fun initTabLayoutAndViewPager() {
        // 初始化tabLayout
        val tabItems = listOf("简介","评论")
        binding.tabs.isTabIndicatorFullWidth = false
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.tabs.setSelectedTabIndicatorColor(
            ResourcesCompat.getColor(
                resources,
                R.color.green_300, null
            )
        )
        // 初始化viewPage
        val adapter = VideoPlayViewPageAdapter(this, tabItems.size, id)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = tabItems[position]
            // 去除长按提示，拦截长按事件
            tab.view.setOnLongClickListener { true }
        }.attach()
    }

    private fun initPlayer() {
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, binding.videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        gsyVideoOption
//            .setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setCacheWithPlay(false)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils.isEnable = true
                    isPlay = true
                }

                override fun onClickResume(url: String?, vararg objects: Any?) {
                    binding.main.resetStatus()
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    orientationUtils.backToProtVideo()
                }
            }).setLockClickListener { _, lock ->
                orientationUtils.isEnable = !lock
            }

        // 进入全屏
        binding.videoPlayer.fullscreenButton.setOnClickListener(View.OnClickListener { //直接横屏
            orientationUtils.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            binding.videoPlayer.startWindowFullscreen(this, true, true)
        })

        // 退出
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                orientationUtils.backToProtVideo()
                if (!GSYVideoManager.backFromWindowFull(this@VideoPlayerActivity)) {
                    //退出应用
                    finish()
                }
            }
        })
    }

    private fun calculateVideoViewHeight(): Float {
        // 获取屏幕宽度
        val screenWidth = DisplayUtil.getScreenWidth(this)
        // 计算宽高比
        val aspectRatio = 9f / 16f // TODO:这里写死为16 ：9，因为这需要服务端提前返回视频宽高比
        // 计算VideoView的高度
        return (screenWidth * aspectRatio)
    }

    override fun onPause() {
        binding.videoPlayer.getCurrentPlayer().onVideoPause()
        super.onPause()
    }

    override fun onResume() {
//        binding.videoPlayer.getCurrentPlayer().onVideoResume(false)
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            binding.videoPlayer.getCurrentPlayer().release()
        }
        orientationUtils.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏,binding.videoPlayer.currentState != GSYVideoPlayer.CURRENT_STATE_PAUSE
        if (isPlay) {
            binding.videoPlayer.onConfigurationChanged(
                this,
                newConfig,
                orientationUtils,
                true,
                true
            )
        }
    }

    override suspend fun sendBarrage(bi: BarrageInfo): Boolean {
        // TODO 向服务器提交弹幕数据
        // 显示弹幕
        binding.videoPlayer.mDanmakuController.addFakeData(TextData().apply {
            text = barrageInfo.message
            layerType = when(barrageInfo.position) {
                "滚动" -> LAYER_TYPE_SCROLL
                "置顶" -> LAYER_TYPE_TOP_CENTER
                "置底" -> LAYER_TYPE_BOTTOM_CENTER
                else -> LAYER_TYPE_SCROLL
            }
            textColor = Color.parseColor(barrageInfo.color)
            textSize =  if (barrageInfo.size == "默认") DisplayUtil.spToPx(this@VideoPlayerActivity,20f).toFloat() else DisplayUtil.spToPx(this@VideoPlayerActivity,12f).toFloat()
        })
        return true
    }
}