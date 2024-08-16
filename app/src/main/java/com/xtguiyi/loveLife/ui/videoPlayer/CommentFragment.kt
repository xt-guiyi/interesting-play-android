package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.databinding.FragmentCommentBinding
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.CommentDialogFragment
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoPlayerViewModel
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "id"

class CommentFragment : BaseFragment() {
    private var id: String? = null
    private lateinit var binding: FragmentCommentBinding
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

    override fun initView() {
    }

    override fun initData() {
        val pH = DisplayUtil.dip2px(requireContext(),12f)
        lifecycleScope.launch {
            videoPlayerViewModel.navHeight.collectLatest{
                binding.commentContainer.setPadding(pH,pH,pH, it)
            }
        }
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