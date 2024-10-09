package com.xtguiyi.play.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.play.databinding.ItemEmojiItem2Binding
import com.xtguiyi.play.utils.GlideUtil

class EmojiItem2Adapter(private var list: List<String>): RecyclerView.Adapter<EmojiItem2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmojiItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindTo(list[position])
        holder.binding.root.setOnClickListener{
            mClickCall?.invoke(list[position])
        }
    }

    class ViewHolder(val binding:ItemEmojiItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(text: String){
            GlideUtil.loadUrlNoDiskCache(text, binding.root.context, binding.emojiImage) // 不缓存才能加载
        }
    }

    private var mClickCall: ((bitmap: String) -> Unit)? = null

    public fun setOnClickListenerByRoot(mClickCall: (bitmap: String) -> Unit) {
        this.mClickCall = mClickCall
    }
}
