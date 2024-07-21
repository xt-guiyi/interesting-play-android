package com.example.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loveLife.base.BaseFragment
import com.example.loveLife.databinding.FragmentVideoBriefIntroductionBinding
import com.example.loveLife.ui.home.adapter.RelateVideoCardAdapter
import com.example.loveLife.ui.videoPlayer.adapter.VideoBriefIntroductionAdapter
import com.example.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "id"

class VideoBriefIntroductionFragment : BaseFragment() {
    private var id: Int? = null
    private lateinit var binding: FragmentVideoBriefIntroductionBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var videoBriefIntroductionAdapter: VideoBriefIntroductionAdapter
    private lateinit var relateVideoCardAdapter: RelateVideoCardAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val viewModel: VideoBriefIntroductionViewModel by activityViewModels()

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
        binding = FragmentVideoBriefIntroductionBinding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        // 设置线性布局
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.briefIntroductionRv.layoutManager = layoutManager

        // 设置适配器
        videoBriefIntroductionAdapter = VideoBriefIntroductionAdapter(mutableListOf())
        relateVideoCardAdapter = RelateVideoCardAdapter(mutableListOf())
        concatAdapter = ConcatAdapter(videoBriefIntroductionAdapter, relateVideoCardAdapter)
        binding.briefIntroductionRv.adapter = concatAdapter
    }

    override fun initData() {
        lifecycleScope.launch {
            id?.let {
               viewModel.getVideoDetail(it)
                viewModel.getVideoList()
            }
            viewModel.uiStateFlow.collect { uiState ->
                uiState.videoInfo?.let {
                    videoBriefIntroductionAdapter.setData(it)

                }
                uiState.relatedVideoInfoList?.let {
                    relateVideoCardAdapter.addItems(it)

                    if(relateVideoCardAdapter.itemCount == uiState.total) {
                        delay(500)
                        binding.smRefreshLayout.finishLoadMoreWithNoMoreData()
                    }else {
                        binding.smRefreshLayout.resetNoMoreData()
                    }
                }
            }
        }
    }

    override fun bindingListener() {
        // 加载更多
        binding.smRefreshLayout.setOnLoadMoreListener {
            lifecycleScope.launch {
                viewModel.loadMore()
                it.finishLoadMore(true)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            VideoBriefIntroductionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, id)
                }
            }
    }
}