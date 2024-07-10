package com.example.lovelife

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import com.example.loveLife.R
import com.example.loveLife.databinding.ActivityMainBinding
import com.example.lovelife.base.BaseActivity
import com.example.lovelife.ui.discover.DiscoverFragment
import com.example.lovelife.ui.home.HomeFragment
import com.example.lovelife.ui.me.MeFragment
import com.example.lovelife.utils.FragmentUtil

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val homeFragment by lazy { HomeFragment() }
    private val discoverFragment by lazy { DiscoverFragment() }
    private val meFragment by lazy { MeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        init()
        bindingListener()
        setContentView(binding.root)
    }

    override fun init() {
        // 初始化Fragment
        FragmentUtil
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, homeFragment,"Home")
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, discoverFragment,"Discover")
            .addFragment(supportFragmentManager, binding.mainFragmentContainer.id, meFragment,"Me")
            .hideFragment(supportFragmentManager, discoverFragment)
            .hideFragment(supportFragmentManager, meFragment)
    }

    override fun bindingListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            val currentFragment = FragmentUtil.getCurrentFragmentByContainerId(supportFragmentManager, binding.mainFragmentContainer.id)
            when (it.itemId) {
                R.id.menu_home -> {
                    if(currentFragment != null && currentFragment.tag != "Home") {
                        Log.i("bottomNavigationItem","首页")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, homeFragment)
                    }
                }
                R.id.menu_discover -> {
                    if(currentFragment != null && currentFragment.tag != "Discover") {
                        Log.i("bottomNavigationItem","发现")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, discoverFragment)
                    }
                }
                R.id.menu_me -> {
                    if(currentFragment != null && currentFragment.tag != "Me") {
                        Log.i("bottomNavigationItem","我的")
                        FragmentUtil.hideFragment(supportFragmentManager, currentFragment)
                        FragmentUtil.showFragment(supportFragmentManager, meFragment)
                    }
                }
                else -> {}
            }
            return@setOnItemSelectedListener true
        }
    }
}