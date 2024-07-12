package com.example.lovelife.ui.home.components.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loveLife.databinding.FragmentHomeViewPageRvItem1Binding
import com.example.lovelife.entity.VideoInfo
import com.example.lovelife.utils.GlideUtil

class CardAdapter(private val list: List<VideoInfo>): RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = FragmentHomeViewPageRvItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(private val binding: FragmentHomeViewPageRvItem1Binding):RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: VideoInfo){
            binding.title.text = data.title
            binding.author.text = data.author
            binding.time.text = data.duration.toString()
            binding.videoNumber.text = data.views.toString()
            GlideUtil.loadUrl(data.url, binding.imgFragment.context, binding.imgFragment)
        }
    }
}