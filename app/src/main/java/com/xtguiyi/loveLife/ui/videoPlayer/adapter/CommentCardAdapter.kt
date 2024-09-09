package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.ItemCommentCardBinding
import com.xtguiyi.loveLife.entity.CommentInfo
import com.xtguiyi.loveLife.utils.GlideUtil
import com.xtguiyi.loveLife.utils.TimeUtil

class CommentCardAdapter(private var list: List<CommentInfo>): RecyclerView.Adapter<CommentCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindTo(list[position])
       holder.binding.commentContent.setOnClickListenerExpandChange{
//            notifyItemChanged(position,1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }


    class ViewHolder(val binding:ItemCommentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: CommentInfo){
//             binding.commentText.text = data.title
            GlideUtil.setUrlCircle("https://himg.bdimg.com/sys/portrait/item/pp.1.7c5c476b.2CynSFe9r91wua7eUYwRAA.jpg?tt=1725802230061", binding.root.context, binding.avatarCircle)
            binding.userName.text = "小王同学"
            binding.publishTime.text = TimeUtil.getTimeAgo(1725689192073)
            binding.commentContent.text = "\uD83D\uDE02哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈\uD83D\uDE02哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈\uD83D\uDE02哈哈哈哈哈哈哈\uD83D\uDE02哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02"

        }
    }
}
