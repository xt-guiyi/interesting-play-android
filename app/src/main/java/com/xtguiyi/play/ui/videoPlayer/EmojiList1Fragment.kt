package com.xtguiyi.play.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.xtguiyi.play.base.BaseFragment
import com.xtguiyi.play.databinding.FragmentEmojiList1Binding
import com.xtguiyi.play.ui.videoPlayer.adapter.EmojiItem1Adapter
import com.xtguiyi.play.ui.videoPlayer.viewModel.CommentDialogViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class EmojiList1Fragment : BaseFragment() {
    private lateinit var binding: FragmentEmojiList1Binding
    private val commentDialogViewModel: CommentDialogViewModel by viewModels({requireParentFragment()})
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
        layoutManager = GridLayoutManager(requireContext(),6)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        try {
            requireContext().assets.open("Emoji.json").use {
                val emojiList = Json.decodeFromString<List<String>>(it.bufferedReader().readText())
                val adapter = EmojiItem1Adapter(emojiList)
                binding.rv.adapter = adapter
                adapter.setOnClickListenerByRoot {
                    lifecycleScope.launch{ commentDialogViewModel.setEmojiStr(it)}
                }
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