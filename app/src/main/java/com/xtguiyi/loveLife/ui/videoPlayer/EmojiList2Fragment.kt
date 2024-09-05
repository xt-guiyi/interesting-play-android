package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.databinding.FragmentEmojiList2Binding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiItem2Adapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.CommentDialogViewModel
import kotlinx.coroutines.launch


class EmojiList2Fragment : BaseFragment() {
    private lateinit var binding: FragmentEmojiList2Binding
    private lateinit var layoutManager: GridLayoutManager
    private val commentDialogViewModel: CommentDialogViewModel by viewModels({requireParentFragment()})

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
        layoutManager = GridLayoutManager(requireContext(),6)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        try {
                val emojiList = listOf(
                    "https://images.cubox.pro/iw3rni/file/2024090514272761607/television_1.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514273420039/television_2.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514273915649/television_3.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514274495812/television_4.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514274955257/television_5.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514275450097/television_6.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514280920334/television_7.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514283776016/television_8.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090514284630902/television_10.webp",
                )
                val adapter = EmojiItem2Adapter(emojiList)
                binding.rv.adapter = adapter
                adapter.setOnClickListenerByRoot {
                   lifecycleScope.launch{ commentDialogViewModel.setEmojiStr("哈哈哈👀")}
                }
        } catch (e: Exception) {
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
            EmojiList2Fragment()
    }
}