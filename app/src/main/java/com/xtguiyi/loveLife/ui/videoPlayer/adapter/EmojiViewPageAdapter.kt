package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.BriefIntroductionFragment
import com.xtguiyi.loveLife.ui.videoPlayer.CommentFragment
import com.xtguiyi.loveLife.ui.videoPlayer.EmojiList1Fragment
import com.xtguiyi.loveLife.ui.videoPlayer.EmojiList2Fragment

class EmojiViewPageAdapter(private val fragmentActivity: FragmentActivity, private val size :Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) EmojiList1Fragment.newInstance() else EmojiList2Fragment.newInstance()
    }
}