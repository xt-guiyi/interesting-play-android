package com.xtguiyi.loveLife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.forEach
import com.xtguiyi.loveLife.base.BaseActivity
import com.xtguiyi.loveLife.databinding.ActivityMainBinding
import com.xtguiyi.loveLife.ui.discover.DiscoverFragment
import com.xtguiyi.loveLife.ui.home.HomeFragment
import com.xtguiyi.loveLife.ui.me.MeFragment
import com.xtguiyi.loveLife.ui.videoPlayer.VideoPlayerActivity
import com.xtguiyi.loveLife.utils.FragmentUtil

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val homeFragment by lazy { HomeFragment() }
    private val discoverFragment by lazy { DiscoverFragment() }
    private val meFragment by lazy { MeFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 重要：配置更改就不需要再添加Fragment
        if(savedInstanceState == null) { initView() }
        bindingListener()
    }

    override fun initView() {
        // 初始化Fragment
        FragmentUtil
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, homeFragment,"Home")
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, discoverFragment,"Discover")
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, meFragment,"Me")
            .hideFragment(supportFragmentManager, discoverFragment)
            .hideFragment(supportFragmentManager, meFragment)
    }

    override fun initData() {}

    override fun bindingListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment =
                FragmentUtil.getCurrentFragmentByContainerId(supportFragmentManager, binding.mainFragmentContainer.id)
                    ?: return@setOnItemSelectedListener true
            when (it.itemId) {
                R.id.menu_home -> {
                    if(currentFragment.tag != "Home") {
                        Log.i("bottomNavigationItem","首页")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, "Home")
                    }
                }
                R.id.menu_discover -> {
                    if(currentFragment.tag != "Discover") {
                        Log.i("bottomNavigationItem","发现")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, "Discover")
                    }
                }
                R.id.menu_me -> {
                    if(currentFragment.tag != "Me") {
                        Log.i("bottomNavigationItem","我的")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, "Me")
                    }
                }
            }
            return@setOnItemSelectedListener true
        }
        // 去除长按提示，拦截长按事件
        binding.bottomNavigation.menu.forEach {
            val menuItemView: View = findViewById(it.itemId) // findViewById必须在setContentView之后调用
            menuItemView.setOnLongClickListener { true }
        }
    }
}