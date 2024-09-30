package com.xtguiyi.loveLife.ui.discover.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.databinding.ItemDynamicsCardBinding
import com.xtguiyi.loveLife.model.DiscoverInfo
import com.xtguiyi.loveLife.utils.CommonUtil
import com.xtguiyi.loveLife.utils.DisplayUtil
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

        private var cachedItemHeight: Int? = null

        private fun calculateItemHeights(): Int {
            if (cachedItemHeight == null) {
                cachedItemHeight = (DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(18f)) / 2
            }
            return cachedItemHeight!!
        }

        fun bindTo(data: DiscoverInfo){
            binding.title.text = data.title
            binding.author.text = data.owner.name
            binding.reply.text = CommonUtil.formatNumber(data.reply)
            // 瀑布流布局，图片最好能够知道提前知道高度，然后手动设置imageView的高度，这样能够避免滚动时，动态计算高度导致发生item位置变化动画，
            //  获取高度方式有两种一种是服务器接口直接告诉图片宽高信息(最优解)，一种是已同步方式先下载图片，拿到图片宽高信息后才设置imageView的高度(慢，费流量)
            // https://juejin.cn/post/7125615887784083469
            if(data.picH != null && data.picW != null) {
                val rate = data.picW / data.picH.toFloat()
                binding.pic.layoutParams.height = (calculateItemHeights() / rate).toInt()
            }
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