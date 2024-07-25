package com.xtguiyi.loveLife.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.BannerCardBinding
import com.xtguiyi.loveLife.entity.Banner
import com.xtguiyi.loveLife.utils.DisplayUtil
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator

class BannerContainerAdapter(private val bannerData: MutableList<Banner>, private val lifecycleOwner: LifecycleOwner, private val context: Context): RecyclerView.Adapter<BannerContainerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BannerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(bannerData, lifecycleOwner, context)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(private val binding: BannerCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindTo(list: List<Banner>,lifecycleOwner: LifecycleOwner, context: Context) {
            binding.bannerContainer.viewPager2.offscreenPageLimit = 1
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