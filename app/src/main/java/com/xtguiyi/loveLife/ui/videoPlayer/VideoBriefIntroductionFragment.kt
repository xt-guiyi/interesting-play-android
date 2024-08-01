package com.xtguiyi.loveLife.ui.videoPlayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.base.BaseFragment
import com.xtguiyi.loveLife.common.adapter.FooterAdapter
import com.xtguiyi.loveLife.databinding.FragmentVideoBriefIntroductionBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.RelateVideoCardAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoBriefIntroductionAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.VideoBriefIntroductionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "id"

class VideoBriefIntroductionFragment : BaseFragment() {
    private var id: Int? = null
    private lateinit var binding: FragmentVideoBriefIntroductionBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var videoBriefIntroductionAdapter: VideoBriefIntroductionAdapter
    private lateinit var relateVideoCardAdapter: RelateVideoCardAdapter
    private lateinit var footerAdapter: FooterAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val viewModel: VideoBriefIntroductionViewModel by viewModels()
    // 分页加载参数
    var scrollDirection = -1 // 滚动方向
    val prefetchDistance = 10 // 提前量
    var loadFinish = false // 加载完毕，没有更多数据了
    var loading = false

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
        binding.rv.layoutManager = layoutManager

        // 设置适配器
        videoBriefIntroductionAdapter = VideoBriefIntroductionAdapter(mutableListOf())
        relateVideoCardAdapter = RelateVideoCardAdapter(mutableListOf())
         footerAdapter =  FooterAdapter(){
            Toaster.show("重试")
        }
        concatAdapter = ConcatAdapter(videoBriefIntroductionAdapter, relateVideoCardAdapter, footerAdapter)
        binding.rv.adapter = concatAdapter
    }

    override fun initData() {
        lifecycleScope.launch {
            id?.let {
               viewModel.getVideoDetail(it)
                viewModel.getVideoList()
            }

            launch {
                viewModel.uiStateFlow.collect { uiState ->
                    uiState.netWorkError.let {
                        loading = false
                        footerAdapter.setStatus(loading = false, error = it)
                    }
                }
            }

            launch {
                viewModel.videoInfoFlow.collect{
                    it?.let {
                        videoBriefIntroductionAdapter.setData(it)

                    }
                }
            }

            launch {
                viewModel.relatedVideoInfoListFlow.collect{
                    loading = false
                    footerAdapter.setStatus(loading = false, error = false)
                    relateVideoCardAdapter.addItems(it)
                    Log.i("onScrollStateChanged", " - ${viewModel.uiStateFlow.value.total} - ${relateVideoCardAdapter.itemCount}")
                    loadFinish = relateVideoCardAdapter.itemCount == viewModel.uiStateFlow.value.total
                }
            }
        }
    }

    override fun bindingListener() {
        // 加载更多
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 scrollDirection = if (dy > 0) 1 else -1
                // 判断是否加载完毕，以及正在加载中
                if (loadFinish || loading) return
                    // 判断是向着列表尾部滚动，并且临界点已经显示，可以加载更多数据。
                    //  1是底部加载占位，需要排除
                    if(scrollDirection == 1 && layoutManager.findViewByPosition(layoutManager.itemCount - 1 - prefetchDistance) != null) {
                        lifecycleScope.launch {
                            loading = true
                            footerAdapter.setStatus(true, error = false)
                            delay(5000)
                            viewModel.loadMore()
                        }
                    }
            }
        })
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