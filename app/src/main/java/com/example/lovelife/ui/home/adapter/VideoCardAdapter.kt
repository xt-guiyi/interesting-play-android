package com.example.lovelife.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loveLife.databinding.FragmentHomeViewPageRvItem1Binding
import com.example.lovelife.entity.VideoInfo
import com.example.lovelife.utils.CommonUtil
import com.example.lovelife.utils.GlideUtil
import com.example.lovelife.utils.TimeUtil
import com.google.android.material.imageview.ShapeableImageView

class VideoCardAdapter(private val videoData: MutableList<VideoInfo>): RecyclerView.Adapter<VideoCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = FragmentHomeViewPageRvItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(videoData[position])
    }

    override fun getItemCount(): Int {
       return videoData.size
    }

    class ViewHolder(private val binding: FragmentHomeViewPageRvItem1Binding):RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: VideoInfo){
            binding.title.text = data.title
            binding.author.text = data.author
            binding.time.text = TimeUtil.geDurationTime(data.duration)
            binding.videoNumber.text = CommonUtil.formatNumber(data.views)
            GlideUtil.loadUrl(data.url, binding.imgFragment.context, binding.imgFragment as ShapeableImageView) // 必须指定为ShapeableImageView
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun restItems(newItems: List<VideoInfo>) {
        videoData.clear()
        videoData.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<VideoInfo>) {
        val startPosition = videoData.size
        videoData.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}