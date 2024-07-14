package com.example.lovelife.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.loveLife.R
import com.example.loveLife.databinding.FragmentHomeBinding
import com.example.lovelife.base.BaseFragment
import com.example.lovelife.ui.home.adapter.HomeViewPageAdapter
import com.example.lovelife.utils.GlideUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val searchTextList = mutableListOf<String>(
        "拜登把泽连斯基叫成普京",
        "原神肯德基套餐上线",
        "macbookair13寸和15寸差别多大",
        "绝区零KDA双厨狂喜",
        "北伐是什么梗",
        "神偷奶爸今日上映",
        "通往夏天的隧道",
        "安卓开发"
    )

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
        initHeader()
        initTabLayoutAndViewPager()
    }

    private fun initHeader() {
        GlideUtil.setUrlCircle(
            "https://images.cubox.pro/iw3rni/file/2024061800331149633/IMG_0021.JPG",
            requireContext(),
            binding.includedHeader.avatar
        )
        // 定时切换
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                withContext(Dispatchers.Main) {
                    binding.includedHeader.searchText.text = searchTextList.random()
                }
                delay(8000)
            }
        }
    }

    private fun initTabLayoutAndViewPager() {
        // 初始化tabLayout
        val tabItems = listOf("推荐","小说","漫画","游戏","音乐","舞蹈","萌宠","其他")
        binding.homeTabs.tabMode = TabLayout.MODE_SCROLLABLE
        binding.homeTabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.homeTabs.setSelectedTabIndicatorColor(ResourcesCompat.getColor(resources,R.color.green_300,null))
        binding.homeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
            val tabTextView = TextView(requireContext())
            tabTextView.text = tabItems[position]
            tabTextView.setTextAppearance(R.style.TabItemTextStyle)
            // 去除长按提示，拦截长按事件
            tabTextView.setOnLongClickListener { true }
            tab.setCustomView(tabTextView);

        }.attach()
    }

    override fun bindingListener() {}
}