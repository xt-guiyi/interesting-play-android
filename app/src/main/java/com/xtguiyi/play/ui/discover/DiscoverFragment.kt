package com.xtguiyi.play.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xtguiyi.play.R
import com.xtguiyi.play.base.BaseFragment
import com.xtguiyi.play.databinding.FragmentDiscoverBinding
import com.xtguiyi.play.ui.discover.adapter.DiscoverViewPageAdapter

class DiscoverFragment : BaseFragment() {
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        // 初始化tabLayout
        val tabItems = listOf("附近","同城","关注")
        binding.discoverTabs.isTabIndicatorFullWidth = false
        binding.discoverTabs.tabMode = TabLayout.MODE_FIXED
        binding.discoverTabs.tabGravity = TabLayout.GRAVITY_CENTER
        binding.discoverTabs.setSelectedTabIndicatorColor(ResourcesCompat.getColor(resources,R.color.green_300,null))
        binding.discoverTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.let { textView ->
                    if (textView is TextView) {
                        textView.setTextAppearance(R.style.TabItemSelectedTextStyle)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.let { textView ->
                    if (textView is TextView) {
                        textView.setTextAppearance(R.style.TabItemTextStyle)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        // 初始化viewPage
        val adapter = DiscoverViewPageAdapter(this, tabItems.size)
        binding.discoverViewpager.adapter = adapter
        binding.discoverViewpager.offscreenPageLimit = 3 // 离屏加载，左右缓存3个

        TabLayoutMediator(binding.discoverTabs,binding.discoverViewpager) { tab, position ->
            val tabTextView = TextView(requireContext())
            tabTextView.text = tabItems[position]
            tabTextView.setTextAppearance(R.style.TabItemTextStyle)
            // 去除长按提示，拦截长按事件
            tabTextView.setOnLongClickListener { true }
            tab.setCustomView(tabTextView);

        }.attach()
    }

    override fun initData() {

    }

    override fun bindingListener() {}

    companion object {
        @JvmStatic
        fun newInstance() =
            DiscoverFragment()
    }
}