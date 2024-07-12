package com.example.lovelife.ui.home.components.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.loveLife.R
import com.example.loveLife.databinding.FragmentHomeViewPageRvItem2Binding
import com.youth.banner.indicator.CircleIndicator

class BannerContainerAdapter(private val list: List<String>, private val lifecycleOwner: LifecycleOwner,private val context: Context): RecyclerView.Adapter<BannerContainerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentHomeViewPageRvItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(list, lifecycleOwner, context)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(private val binding: FragmentHomeViewPageRvItem2Binding): RecyclerView.ViewHolder(binding.root) {
        fun bindTo(list: List<String>,lifecycleOwner: LifecycleOwner, context: Context) {
            binding.bannerContainer
                .setAdapter(BannerDataAdapter(list,context))
                .addBannerLifecycleObserver(lifecycleOwner)
                .setIndicator(CircleIndicator(context))
                .setIndicatorSelectedColorRes(R.color.green_300)
                .setIndicatorNormalColorRes(R.color.sliver_200)
        }
    }

}