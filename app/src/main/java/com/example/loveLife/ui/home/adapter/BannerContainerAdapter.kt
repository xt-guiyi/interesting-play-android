package com.example.loveLife.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.loveLife.R
import com.example.loveLife.databinding.FragmentHomeViewPageRvItem2Binding
import com.example.loveLife.entity.Banner
import com.example.loveLife.utils.DisplayUtil
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator

class BannerContainerAdapter(private val bannerData: MutableList<Banner>, private val lifecycleOwner: LifecycleOwner, private val context: Context): RecyclerView.Adapter<BannerContainerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentHomeViewPageRvItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(bannerData, lifecycleOwner, context)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(private val binding: FragmentHomeViewPageRvItem2Binding): RecyclerView.ViewHolder(binding.root) {
        fun bindTo(list: List<Banner>,lifecycleOwner: LifecycleOwner, context: Context) {
            binding.bannerContainer
                .setAdapter(BannerDataAdapter(list))
                .addBannerLifecycleObserver(lifecycleOwner)
                .setLoopTime(5000)
                .setIndicator(CircleIndicator(context))
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                .setIndicatorMargins(IndicatorConfig.Margins(
                    0,
                    0,
                    DisplayUtil.dip2px(context,20f),
                    DisplayUtil.dip2px(context,8f)
                ))
                .setIndicatorWidth( DisplayUtil.dip2px(context,8f),DisplayUtil.dip2px(context,8f))
                .setIndicatorSelectedColorRes(R.color.green_300)
                .setIndicatorNormalColorRes(R.color.sliver_100)
        }
    }

    fun setItem(newItems: List<Banner>) {
        bannerData.clear()
        bannerData.addAll(newItems)
        notifyItemChanged(0)
    }
}