package com.xtguiyi.loveLife.ui.discover.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.ItemDynamicsCardBinding
import com.xtguiyi.loveLife.model.DiscoverInfo
import com.xtguiyi.loveLife.utils.CommonUtil
import com.xtguiyi.loveLife.utils.GlideUtil

class DynamicsCardAdapter(private val list: MutableList<DiscoverInfo>): RecyclerView.Adapter<DynamicsCardAdapter.ViewHolder>() {
    private var mClickCall: ((view: View,position: Int, list: DiscoverInfo) -> Unit)? = null

    fun setOnClickListenerByRoot(mClickCall: (view: View,position: Int, list: DiscoverInfo) -> Unit) {
        this.mClickCall = mClickCall
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemDynamicsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(list[position])
        holder.binding.root.setOnClickListener {
            mClickCall?.invoke(it, position, list[position])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(val binding: ItemDynamicsCardBinding):RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: DiscoverInfo){
            binding.title.text = data.title
            binding.author.text = data.owner.name
            binding.reply.text = CommonUtil.formatNumber(data.reply)
            GlideUtil.loadUrl(data.pic, binding.pic.context, binding.pic)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun restItems(newItems: List<DiscoverInfo>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<DiscoverInfo>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}