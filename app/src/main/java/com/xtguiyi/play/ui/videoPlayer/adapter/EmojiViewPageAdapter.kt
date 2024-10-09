package com.xtguiyi.play.ui.videoPlayer.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xtguiyi.play.ui.videoPlayer.EmojiList1Fragment
import com.xtguiyi.play.ui.videoPlayer.EmojiList2Fragment

class EmojiViewPageAdapter(private val fragment: Fragment, private val size :Int): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) EmojiList1Fragment.newInstance() else EmojiList2Fragment.newInstance()
    }
}