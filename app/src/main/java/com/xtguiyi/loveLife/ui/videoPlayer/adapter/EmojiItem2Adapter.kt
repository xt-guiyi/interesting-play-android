package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.LayoutEmojiItem2Binding
import com.xtguiyi.loveLife.utils.GlideUtil

class EmojiItem2Adapter(private var list: List<String>): RecyclerView.Adapter<EmojiItem2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutEmojiItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(val binding:LayoutEmojiItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(text: String){
            GlideUtil.loadUrlNoCache(text, binding.root.context, binding.emojiImage)
        }
    }

    private var mClickCall: ((str: String) -> Unit)? = null

    public fun setOnClickListenerByRoot(mClickCall: (str: String) -> Unit) {
        this.mClickCall = mClickCall
    }
}
