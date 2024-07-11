package com.example.lovelife.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.loveLife.R
import com.example.loveLife.databinding.FragmentHomeBinding
import com.example.lovelife.base.BaseFragment
import com.example.lovelife.ui.home.components.ViewPageType1Fragment
import com.example.lovelife.ui.home.components.ViewPageType2Fragment
import com.example.lovelife.utils.GlideUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun init() {
        GlideUtil.setUrlCircle("https://images.cubox.pro/iw3rni/file/2024061800331149633/IMG_0021.JPG", requireContext(), binding.includedHeader.avatar)
        initTabLayoutAndViewPager()
    }

    private fun initTabLayoutAndViewPager() {
        val tabItems = listOf("推荐","小说","漫画","游戏","音乐","舞蹈","萌宠","其他")
        binding.homeTabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.homeTabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.homeTabs.setSelectedTabIndicatorColor(ResourcesCompat.getColor(resources,R.color.green_300,null))

        val fragments = listOf(
            ViewPageType1Fragment.newInstance("1"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
            ViewPageType2Fragment.newInstance("敬请期待"),
        )

        val adapter = HomeViewPageAdapter(this, fragments)
        binding.homeViewpager.adapter = adapter

        TabLayoutMediator(binding.homeTabs,binding.homeViewpager) { tab, position ->
            tab.text = tabItems[position]
        }.attach()
    }

    override fun bindingListener() {
    }
}