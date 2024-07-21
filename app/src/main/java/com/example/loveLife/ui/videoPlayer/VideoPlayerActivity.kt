package com.example.loveLife.ui.videoPlayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.loveLife.R
import com.example.loveLife.base.BaseActivity
import com.example.loveLife.databinding.ActivityVideoPlayerBinding
import com.example.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.example.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class VideoPlayerActivity : BaseActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val viewModel: VideoBriefIntroductionViewModel by viewModels()
    private val id: Int by lazy {
        intent.getIntExtra("id", -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        initView()
        initData()
        setContentView(binding.root)
    }

    override fun initView() {
        initTabLayoutAndViewPager()
    }

    override fun initData() {
        lifecycleScope.launch {
            viewModel.uiStateFlow.collect { uiState ->
                uiState.videoInfo?.let {
                    binding.videoPlayer.setVideoURI(Uri.parse(it.url))
                    binding.videoPlayer.start()
                }
            }
        }
    }

    override fun bindingListener() {
        TODO("Not yet implemented")
    }

    private fun initTabLayoutAndViewPager() {
        // 初始化tabLayout
        val tabItems = listOf("简介")
        binding.videoPlayerTabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.videoPlayerTabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.videoPlayerTabs.setSelectedTabIndicatorColor(
            ResourcesCompat.getColor(resources,
                R.color.green_300,null))

        // 初始化viewPage
        val adapter = VideoPlayViewPageAdapter(this, tabItems.size,id)
        binding.videoPlayerViewpager.adapter = adapter

        TabLayoutMediator(binding.videoPlayerTabs,binding.videoPlayerViewpager) { tab, position ->
            tab.text = tabItems[position]
            // 去除长按提示，拦截长按事件
            tab.view.setOnLongClickListener { true }
        }.attach()
    }
}