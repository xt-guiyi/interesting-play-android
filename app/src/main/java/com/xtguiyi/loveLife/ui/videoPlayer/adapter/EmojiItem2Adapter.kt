package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.LayoutEmojiItem2Binding
import com.xtguiyi.loveLife.utils.DisplayUtil
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
            val round = DisplayUtil.dip2px(holder.binding.root.context, 22f)
            val originalBitmap = holder.binding.emojiImage.drawable.toBitmap()
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, round, round, true)
            mClickCall?.invoke(scaledBitmap)
        }
    }

    class ViewHolder(val binding:LayoutEmojiItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(text: String){
            GlideUtil.loadUrlNoCache(text, binding.root.context, binding.emojiImage) // 不缓存才能加载
        }
    }

    private var mClickCall: ((bitmap: Bitmap) -> Unit)? = null

    public fun setOnClickListenerByRoot(mClickCall: (bitmap: Bitmap) -> Unit) {
        this.mClickCall = mClickCall
    }
}
