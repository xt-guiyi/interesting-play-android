package com.example.lovelife.ui.home.components

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loveLife.databinding.FragmentHomeViewPageType1Binding
import com.example.lovelife.base.BaseFragment
import com.example.lovelife.entity.VideoInfo
import com.example.lovelife.ui.home.components.adapter.BannerContainerAdapter
import com.example.lovelife.ui.home.components.adapter.CardAdapter
import com.example.lovelife.utils.DisplayUtil

private const val ARG_PARAM1 = "type"

class HomeViewPageType1Fragment : BaseFragment() {
    private var type: String? = null
    private lateinit var binding: FragmentHomeViewPageType1Binding

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

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
        return binding.root
    }

    override fun init() {
        val gridLayout = GridLayoutManager(requireContext(),2)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if(position == 0) return 2
                return  1
            }
        }
        binding.rv.layoutManager = gridLayout
        val spacing = DisplayUtil.dip2px(requireContext(),2f) // 间距，单位为px
        val includeEdge = true
        binding.rv.addItemDecoration(GridSpacingItemDecoration(2, spacing, includeEdge))
        val bannerList = listOf(
            "https://images.cubox.pro/iw3rni/file/2024061800331149633/IMG_0021.JPG",
            "https://i0.hdslb.com/bfs/banner/c433fb755aba84b3304a839eeb7f7818ccf9dc4c.jpg@976w_550h_1c_!web-home-carousel-cover.avif",
            "https://i0.hdslb.com/bfs/banner/c433fb755aba84b3304a839eeb7f7818ccf9dc4c.jpg@976w_550h_1c_!web-home-carousel-cover.avif",
            "https://i0.hdslb.com/bfs/banner/c433fb755aba84b3304a839eeb7f7818ccf9dc4c.jpg@976w_550h_1c_!web-home-carousel-cover.avif",
        )
        val videoList = listOf(
            VideoInfo("1", "【医学博士】每天凌晨3点睡，多少天会死？| 如何把熬夜危害控制到最小？", 596, "兔叭咯", "https://i2.hdslb.com/bfs/archive/86b5bddaf587bb2f9a43d71c4b368acb3d758e6a.jpg", 3117324),
            VideoInfo("2", "【医学博士】每天凌晨3点睡，多少天会死？| 如何把熬夜危害控制到最小？", 596, "兔叭咯", "https://i2.hdslb.com/bfs/archive/86b5bddaf587bb2f9a43d71c4b368acb3d758e6a.jpg", 3117324),
            VideoInfo("3", "【医学博士】每天凌晨3点睡，多少天会死？| 如何把熬夜危害控制到最小？", 596, "兔叭咯", "https://i2.hdslb.com/bfs/archive/86b5bddaf587bb2f9a43d71c4b368acb3d758e6a.jpg", 3117324),
            VideoInfo("4", "【医学博士】每天凌晨3点睡，多少天会死？| 如何把熬夜危害控制到最小？", 596, "兔叭咯", "https://i2.hdslb.com/bfs/archive/86b5bddaf587bb2f9a43d71c4b368acb3d758e6a.jpg", 3117324),

        )
        val bannerContainerAdapter = BannerContainerAdapter(bannerList, this, requireContext())
        val bardAdapter = CardAdapter(videoList)
        val concatAdapter = ConcatAdapter(bannerContainerAdapter, bardAdapter)
        binding.rv.adapter = concatAdapter
    }

    override fun bindingListener() {
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            HomeViewPageType1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, type)
                }
            }
    }
}