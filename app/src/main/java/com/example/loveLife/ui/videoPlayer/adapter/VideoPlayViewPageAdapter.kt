package com.example.loveLife.ui.videoPlayer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loveLife.ui.videoPlayer.VideoBriefIntroductionFragment

class VideoPlayViewPageAdapter(private val fragmentActivity: FragmentActivity, private val size :Int,private val id:Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return VideoBriefIntroductionFragment.newInstance(id)
    }
}