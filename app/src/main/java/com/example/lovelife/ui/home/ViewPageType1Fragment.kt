package com.example.lovelife.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.loveLife.databinding.FragmentHomeViewPageType1Binding
import com.example.lovelife.base.BaseFragment
import com.example.lovelife.common.GridSpacingItemDecoration
import com.example.lovelife.ui.home.adapter.BannerContainerAdapter
import com.example.lovelife.ui.home.adapter.VideoCardAdapter
import com.example.lovelife.ui.home.viewModel.ViewPageType1ViewModel
import com.example.lovelife.utils.DisplayUtil
import com.hjq.toast.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "type"

class ViewPageType1Fragment : BaseFragment() {
    private var type: String? = null
    private lateinit var binding: FragmentHomeViewPageType1Binding
    private val viewModel: ViewPageType1ViewModel by viewModels()

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
        binding = FragmentHomeViewPageType1Binding.inflate(inflater, container, false)
        init()
        bindingListener()
        return binding.root
    }

    override fun init() {
        // 设置网格布局
        val gridLayout = GridLayoutManager(requireContext(), 2)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 0) return 2
                return 1
            }
        }
        binding.rv.layoutManager = gridLayout
        // 设置间距
        val spacing = DisplayUtil.dip2px(requireContext(), 2f) // 间距，单位为px
        val includeEdge = true
        binding.rv.addItemDecoration(GridSpacingItemDecoration(2, spacing, includeEdge))
        // 设置适配器
        val bannerContainerAdapter = BannerContainerAdapter(mutableListOf(), this, requireContext())
        val videoCardAdapter = VideoCardAdapter(mutableListOf())
        val concatAdapter = ConcatAdapter(bannerContainerAdapter, videoCardAdapter)
        binding.rv.adapter = concatAdapter

        // 设置数据
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                    viewModel.uiStateFlow.collect {
                        if (it.netWorkError) {
//                            Toaster.show("网络错误")
                            binding.smRefreshLayout.visibility = View.INVISIBLE
                            binding.retry.visibility = View.VISIBLE
                        }else{
                            binding.retry.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun bindingListener() {
        binding.smRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            it.finishRefresh(true)
        }

        binding.smRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
            it.finishLoadMore(true)
        }

        binding.retry.setOnClickListener {
            viewModel.refresh()
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