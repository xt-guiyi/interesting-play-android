package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.common.adapter.FooterAdapter
import com.xtguiyi.loveLife.databinding.FragmentCommentBinding
import com.xtguiyi.loveLife.entity.CommentInfo
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.CommentCardAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.CommentDialogFragment
import com.xtguiyi.loveLife.ui.videoPlayer.dialog.CommentDialogFragment.OnCommentListener
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.CommentViewModel
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoPlayerViewModel
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "id"

class CommentFragment : BaseFragment(),OnCommentListener {
    private var id: Int? = null
    private lateinit var binding: FragmentCommentBinding
    private val videoPlayerViewModel: VideoPlayerViewModel by activityViewModels()
    private val viewModel: CommentViewModel by activityViewModels()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var commentCardAdapter: CommentCardAdapter
    private lateinit var footerAdapter: FooterAdapter
    // 分页加载参数
    var scrollDirection = -1 // 滚动方向
    val prefetchDistance = 10 // 提前量
    var customId = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_PARAM1)
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
        val pH = DisplayUtil.dip2px(12f)
        lifecycleScope.launch {
            launch {
                videoPlayerViewModel.navHeight.collectLatest{
                    binding.commentInputContainer.setPadding(pH, pH, pH, it)
                    binding.commentInputContainer.post {
                        binding.rv.setPadding(pH, 0, pH, binding.commentInputContainer.height)

                    }
                }
            }
            launch {
                videoPlayerViewModel.offset.collect{
                    binding.commentInputContainer.translationY = it.toFloat()
                }
            }
        }
        // 设置线性布局
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        // 设置适配器
        commentCardAdapter = CommentCardAdapter(mutableListOf())
        footerAdapter = FooterAdapter {
            Toaster.show("重试")
        }
        val concatAdapter =
            ConcatAdapter(commentCardAdapter)
        binding.rv.adapter = concatAdapter
    }

    override fun initData() {
        lifecycleScope.launch {
            id?.let {
                viewModel.getCommentList()
            }

            launch {
                viewModel.netWorkErrorFlow.collect {
                    if (it) footerAdapter.setStatus(FooterAdapter.LoadResult.Error())

                }
            }


            launch {
                viewModel.commentInfoListFlow.collect {
                    commentCardAdapter.addItems(it)
                    if(commentCardAdapter.itemCount == viewModel.total) {
                        footerAdapter.setStatus(FooterAdapter.LoadResult.NotMore())
                    }else {
                        footerAdapter.setStatus(FooterAdapter.LoadResult.NotLoading())
                    }
                }
            }
        }
    }

    override fun bindingListener() {
        binding.inputBox.setOnClickListener {
                CommentDialogFragment(this).show(requireActivity().supportFragmentManager, CommentDialogFragment.TAG)
        }

        binding.emoteView.setOnClickListener {
            CommentDialogFragment(this,true).show(requireActivity().supportFragmentManager, CommentDialogFragment.TAG)
        }

        // 加载更多
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrollDirection = if (dy > 0) 1 else -1
                // 判断是否加载完毕，以及正在加载中
                if (footerAdapter.getStatus() is FooterAdapter.LoadResult.NotMore || footerAdapter.getStatus() is FooterAdapter.LoadResult.Loading) return
                // 判断是向着列表尾部滚动，并且临界点已经显示，可以加载更多数据。
                //  减1是要减去一个底部加载占位，然后减去提前量
                if (scrollDirection == 1 && layoutManager.findViewByPosition(layoutManager.itemCount - 1 - prefetchDistance) != null) {
                    footerAdapter.setStatus(FooterAdapter.LoadResult.Loading())
                    lifecycleScope.launch {viewModel.loadMore()}
                }
            }
        })
    }

    override suspend fun sendComment(text: Editable?): Boolean {
        text?.let {
            commentCardAdapter.shiftItem(CommentInfo(customId--, "xtguiyi", System.currentTimeMillis(), "广东", text.toString(),"https://images.cubox.pro/1721051466053/240807/image.png",0))
            binding.root.postDelayed({
                layoutManager.scrollToPosition(0)
            },0)
        }
        return true
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