package com.xtguiyi.play.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.xtguiyi.play.base.BaseFragment
import com.xtguiyi.play.databinding.FragmentEmojiList2Binding
import com.xtguiyi.play.ui.videoPlayer.adapter.EmojiItem2Adapter
import com.xtguiyi.play.ui.videoPlayer.viewModel.CommentDialogViewModel
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
                    "https://images.cubox.pro/iw3rni/file/2024090618492887435/television_11.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618561251488/television_12.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618561839245/television_13.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618562339098/television_14.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618562865061/television_15.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618563469694/television_16.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618564049178/television_17.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618564555249/television_18.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618565036064/television_19.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618565445344/television_20.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618565939020/television_21.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618570527550/television_22.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618571123361/television_23.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618571638087/television_24.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618572493157/television_25.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618572851384/television_26.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618573376040/television_27.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618574341562/television_28.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618574812183/television_29.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618575239146/television_30.webp",
                    "https://images.cubox.pro/iw3rni/file/2024090618575669574/television_31.webp",
                )
                val adapter = EmojiItem2Adapter(emojiList)
                binding.rv.adapter = adapter
                adapter.setOnClickListenerByRoot {
                   lifecycleScope.launch{ commentDialogViewModel.setEmojiImage(it)}
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