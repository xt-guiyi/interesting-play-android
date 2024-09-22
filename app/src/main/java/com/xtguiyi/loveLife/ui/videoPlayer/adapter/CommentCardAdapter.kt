package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.ItemCommentCardBinding
import com.xtguiyi.loveLife.model.CommentInfo
import com.xtguiyi.loveLife.utils.CommonUtil
import com.xtguiyi.loveLife.utils.GlideUtil
import com.xtguiyi.loveLife.utils.TimeUtil

class CommentCardAdapter(private var list: MutableList<CommentInfo>): RecyclerView.Adapter<CommentCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindTo(list[position])
    }

    fun addItems(newItems: List<CommentInfo>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun shiftItem(newItem: CommentInfo) {
        list.add(0, newItem)
        notifyItemInserted(0)
    }


    class ViewHolder(val binding:ItemCommentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: CommentInfo){
            GlideUtil.setUrlCircle(data.avatar, binding.root.context, binding.avatarCircle)
            binding.userName.text = data.username
            binding.publishTime.text = TimeUtil.getTimeAgo(data.pubDate)
            binding.commentContent.text = data.content
            binding.dianZanNumber.text = CommonUtil.formatNumber(data.like)
        }
    }
}
