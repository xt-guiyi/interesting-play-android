package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.LayoutCommentCardBinding
import com.xtguiyi.loveLife.entity.CommentInfo

class CommentCardAdapter(private var list: List<CommentInfo>): RecyclerView.Adapter<CommentCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCommentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindTo(list[position])
    }

    class ViewHolder(val binding:LayoutCommentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: CommentInfo){
             binding.commentText.text = data.title
            }
    }
}
