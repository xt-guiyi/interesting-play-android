package com.xtguiyi.loveLife.ui.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.common.ItemDecoration.GridSpacingItemDecoration
import com.xtguiyi.loveLife.databinding.FragmentDiscoverViewPageType1Binding
import com.xtguiyi.loveLife.ui.discover.adapter.DynamicsCardAdapter
import com.xtguiyi.loveLife.ui.discover.viewModel.ViewPageType1ViewModel
import com.xtguiyi.loveLife.ui.videoPlayer.VideoPlayerActivity
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "type"

class ViewPageType1Fragment : BaseFragment() {
    private var type: String? = null
    private lateinit var binding: FragmentDiscoverViewPageType1Binding
    private val viewModel: ViewPageType1ViewModel by viewModels()
    private lateinit var dynamicsCardAdapter: DynamicsCardAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

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
        binding =  FragmentDiscoverViewPageType1Binding.inflate(inflater, container, false)
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun initView() {
        binding.materialHeader.setColorSchemeColors(resources.getColor(R.color.green_300,null))
        // 设置瀑布流布局
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.layoutManager = layoutManager
        // 设置间距
        val spacing = DisplayUtil.dip2px(23f) // 间距，单位为px
        val includeEdge = false
        binding.rv.addItemDecoration(GridSpacingItemDecoration(2, spacing, includeEdge))
        // 设置适配器
        dynamicsCardAdapter = DynamicsCardAdapter(mutableListOf())
        binding.rv.adapter = dynamicsCardAdapter
    }

    override fun initData() {
        lifecycleScope.launch {
            launch {
                viewModel.discoverList.collect {
                    if (viewModel.uiStateFlow.value.updateType == 1) {
                        dynamicsCardAdapter.restItems(it)
                    } else {
                        dynamicsCardAdapter.addItems(it)
                    }

                    if(dynamicsCardAdapter.itemCount == viewModel.uiStateFlow.value.total) {
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

        dynamicsCardAdapter.setOnClickListenerByRoot { view, position, videoData ->
            // 处理点击事件
            val intent = Intent(requireActivity(), VideoPlayerActivity::class.java)
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