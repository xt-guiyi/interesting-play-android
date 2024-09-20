package com.xtguiyi.loveLife.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xtguiyi.loveLife.ui.home.ViewPageType1Fragment
import com.xtguiyi.loveLife.ui.home.ViewPageType2Fragment

class HomeViewPageAdapter(private val ownerFragment: Fragment, private val size :Int): FragmentStateAdapter(ownerFragment) {
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        // 重要：每次应该返回新的fragment实例，而不是从外部接收一个fragment列表，然后一直取对应position位置的fragment实例
        return if(position < 4) {
            ViewPageType1Fragment.newInstance("$position")
        }else {
            ViewPageType2Fragment.newInstance("敬请期待")
        }
    }
}