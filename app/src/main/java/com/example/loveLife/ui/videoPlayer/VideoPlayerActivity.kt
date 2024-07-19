package com.example.loveLife.ui.videoPlayer

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.res.ResourcesCompat
import com.example.loveLife.R
import com.example.loveLife.base.BaseActivity
import com.example.loveLife.databinding.ActivityVideoPlayerBinding
import com.example.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VideoPlayerActivity : BaseActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        initView()
        setContentView(binding.root)
    }

    override fun initView() {
        initTabLayoutAndViewPager()
    }

    override fun initData() {
        TODO("Not yet implemented")
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
        val adapter = VideoPlayViewPageAdapter(this, tabItems.size)
        binding.videoPlayerViewpager.adapter = adapter

        TabLayoutMediator(binding.videoPlayerTabs,binding.videoPlayerViewpager) { tab, position ->
            tab.text = tabItems[position]
            // 去除长按提示，拦截长按事件
            tab.view.setOnLongClickListener { true }
        }.attach()
    }
}