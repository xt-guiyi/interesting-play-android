package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.databinding.FragmentCommentBinding
import com.xtguiyi.loveLife.entity.CommentInfo
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.CommentCardAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.CommentDialogFragment
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoPlayerViewModel
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "id"

class CommentFragment : BaseFragment() {
    private var id: String? = null
    private lateinit var binding: FragmentCommentBinding
    private lateinit var layoutManager: LinearLayoutManager
    private val videoPlayerViewModel: VideoPlayerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun initView() {
        val pH = DisplayUtil.dip2px(requireContext(),12f)
//        binding.rv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
////                binding.rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                Toaster.show("高度：" + binding.rv.height)
//                // 在这里使用 height 的值
//            }
//        })
        lifecycleScope.launch {
            videoPlayerViewModel.navHeight.collectLatest{
                binding.commentInputContainer.setPadding(pH,0,pH, it)
            }
        }
        lifecycleScope.launch {
            videoPlayerViewModel.offset.collect{
                binding.commentInputContainer.translationY =it.toFloat()
            }
        }
        // 设置线性布局
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        val adapter = CommentCardAdapter(mutableListOf(CommentInfo(1,"😂哈哈哈哈哈"),CommentInfo(2,"😂哈哈哈哈哈"),CommentInfo(3,"😂哈哈哈哈哈"),CommentInfo(4,"😂哈哈哈哈哈")))
        binding.rv.adapter = adapter
    }

    override fun initData() {

    }

    override fun bindingListener() {
        binding.inputBox.setOnClickListener {
            val commentDialogFragment = CommentDialogFragment()
            commentDialogFragment.show(requireActivity().supportFragmentManager, CommentDialogFragment.TAG)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, id)
                }
            }
    }
}