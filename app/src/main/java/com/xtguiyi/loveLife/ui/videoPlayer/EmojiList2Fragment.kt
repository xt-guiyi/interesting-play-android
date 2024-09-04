package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.databinding.FragmentEmojiList2Binding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiItem1Adapter
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiItem2Adapter


class EmojiList2Fragment : BaseFragment() {
    private lateinit var binding: FragmentEmojiList2Binding
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmojiList2Binding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        layoutManager = GridLayoutManager(requireContext(), null, 8, GridLayoutManager.HORIZONTAL)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        val adapter = EmojiItem2Adapter(mutableListOf("11", "22"))
        binding.rv.adapter = adapter
    }

    override fun initData() {

    }

    override fun bindingListener() {
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EmojiList2Fragment()
    }
}