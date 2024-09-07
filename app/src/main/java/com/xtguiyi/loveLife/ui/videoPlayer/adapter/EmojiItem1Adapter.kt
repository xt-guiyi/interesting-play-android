package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.ItemEmojiItem1Binding

class EmojiItem1Adapter(private var list: List<String>): RecyclerView.Adapter<EmojiItem1Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmojiItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(val binding:ItemEmojiItem1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(text: String){
             binding.emojiUnicodeText.text = text
        }
    }

    private var mClickCall: ((str: String) -> Unit)? = null

    public fun setOnClickListenerByRoot(mClickCall: (str: String) -> Unit) {
        this.mClickCall = mClickCall
    }
}
