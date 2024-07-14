package com.example.lovelife.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPageAdapter(private val ownerFragment: Fragment, private val fgList: List<Fragment>): FragmentStateAdapter(ownerFragment) {
    override fun getItemCount(): Int {
        return fgList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fgList[position]
        return fragment
    }
}