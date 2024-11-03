package com.xtguiyi.play.ui.videoPlayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.play.databinding.ItemCommentCardBinding
import com.xtguiyi.play.model.CommentInfoModel
import com.xtguiyi.play.utils.CommonUtil
import com.xtguiyi.play.utils.ExpandTextUtil
import com.xtguiyi.play.utils.GlideUtil
import com.xtguiyi.play.utils.TimeUtil

class CommentCardAdapter(private var list: MutableList<CommentInfoModel>): RecyclerView.Adapter<CommentCardAdapter.ViewHolder>() {

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

    fun addItems(newItems: List<CommentInfoModel>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun shiftItem(newItem: CommentInfoModel) {
        list.add(0, newItem)
        notifyDataSetChanged()
    }


    class ViewHolder(val binding:ItemCommentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: CommentInfoModel){
            GlideUtil.setUrlCircle(data.avatar, binding.root.context, binding.avatarCircle)
            binding.userName.text = data.username
            binding.publishTime.text = TimeUtil.getTimeAgo(data.pubDate)
            ExpandTextUtil().setColorStr("#04B578").show(binding.commentContent,  data.content)
            binding.dianZanNumber.text = CommonUtil.formatNumber(data.like)
        }
    }
}
