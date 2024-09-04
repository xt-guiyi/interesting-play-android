package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.databinding.FragmentEmojiList1Binding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiItem1Adapter
import kotlinx.serialization.json.Json
import java.io.IOException


class EmojiList1Fragment : BaseFragment() {
    private lateinit var binding: FragmentEmojiList1Binding
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmojiList1Binding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        // 设置线性布局
        layoutManager = GridLayoutManager(requireContext(),8)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        try {
            requireContext().assets.open("Emoji.json").use {
                val emojiList = Json.decodeFromString<List<String>>(it.bufferedReader().readText())
//                Toaster.show(emojiList)

                val adapter = EmojiItem1Adapter(emojiList)
                binding.rv.adapter = adapter
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initData() {

    }

    override fun bindingListener() {
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EmojiList1Fragment()
    }
}