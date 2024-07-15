package com.example.lovelife.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lovelife.ui.home.ViewPageType1Fragment
import com.example.lovelife.ui.home.ViewPageType2Fragment
import com.hjq.toast.Toaster

class HomeViewPageAdapter(private val ownerFragment: Fragment, private val size :Int): FragmentStateAdapter(ownerFragment) {
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        // 重要：每次应该返回新的fragment实例，而不是从外部接收一个fragment列表，然后一直取对应position位置的fragment实例
        return when (position) {
            0 -> ViewPageType1Fragment.newInstance("1")
            else -> ViewPageType2Fragment.newInstance("敬请期待")
        }
    }
}