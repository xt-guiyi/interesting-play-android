package com.xtguiyi.play.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.xtguiyi.play.base.BaseFragment
import com.xtguiyi.play.databinding.FragmentHomeViewPageType1Binding
import com.xtguiyi.play.ui.home.itemDecoration.GridSpacingItemDecoration
import com.xtguiyi.play.ui.home.adapter.BannerContainerAdapter
import com.xtguiyi.play.ui.home.adapter.VideoCardAdapter
import com.xtguiyi.play.ui.home.viewModel.ViewPageType1ViewModel
import com.xtguiyi.play.ui.videoPlayer.VideoPlayerActivity
import com.xtguiyi.play.utils.DisplayUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "type"

class ViewPageType1Fragment : BaseFragment() {
    private var type: String? = null
    private lateinit var binding: FragmentHomeViewPageType1Binding
    private val viewModel: ViewPageType1ViewModel by viewModels()
    private lateinit var bannerContainerAdapter:BannerContainerAdapter
    private lateinit var videoCardAdapter:VideoCardAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentHomeViewPageType1Binding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        // 设置网格布局
        layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 0 && type == "0") return 2
                return 1
            }
        }
        binding.rv.layoutManager = layoutManager
        // 设置间距
        val spacing = DisplayUtil.dip2px(6f) // 间距，单位为px
        binding.rv.addItemDecoration(GridSpacingItemDecoration(2, spacing))
        // 设置适配器
        bannerContainerAdapter = BannerContainerAdapter(mutableListOf(), this, requireContext())
        videoCardAdapter = VideoCardAdapter(mutableListOf())
        val concatAdapter = when(type) {
            "0" -> ConcatAdapter(bannerContainerAdapter, videoCardAdapter)
            else -> ConcatAdapter(videoCardAdapter)
        }
        binding.rv.adapter = concatAdapter

    }

    override fun initData() {
        lifecycleScope.launch {
                launch {
                    viewModel.bannersFlow.collect {
                        bannerContainerAdapter.setItem(it)
                    }
                }
                launch {
                    viewModel.videoListFlow.collect {
                        if (viewModel.uiStateFlow.value.updateType == 1) {
                            videoCardAdapter.restItems(it)
                        } else {
                            videoCardAdapter.addItems(it)
                        }

                        if(videoCardAdapter.itemCount == viewModel.uiStateFlow.value.total) {
                            delay(500)
                            binding.smRefreshLayout.finishLoadMoreWithNoMoreData()
                        }else {
                            binding.smRefreshLayout.resetNoMoreData()
                        }
                    }
                }

                launch {
                    viewModel.uiStateFlow.collect { uiState ->
                        // 处理网络错误
                        handleVisibility(binding.retry, uiState.netWorkError && !uiState.isLoading)
                        handleVisibility(binding.smRefreshLayout, !uiState.netWorkError)
                        // 处理加载状态
                        handleLoadingAnimation(uiState.isLoading)
                    }
                }
        }
    }

    override fun bindingListener() {
        binding.smRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.refresh()
                it.finishRefresh(true)
            }
        }

        binding.smRefreshLayout.setOnLoadMoreListener {
            lifecycleScope.launch {
                viewModel.loadMore()
                it.finishLoadMore(true)
            }
        }

        binding.retry.setOnClickListener {
            lifecycleScope.launch {
                viewModel.retry()
            }
        }

        videoCardAdapter.setOnClickListenerByRoot { view, position, videoData ->
            // 处理点击事件
            val intent = Intent(requireActivity(), VideoPlayerActivity::class.java)
//            val intent = Intent(requireActivity(), VideoPlayerActivityTwo::class.java)
            intent.putExtra("id", videoData.id)
            startActivity(intent)
        }

    }



    private fun handleVisibility(view: View, shouldShow: Boolean) {
        view.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }

    private fun handleLoadingAnimation(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.loading.playAnimation()
        } else {
            binding.loading.pauseAnimation()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            ViewPageType1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, type)
                }
            }
    }
}