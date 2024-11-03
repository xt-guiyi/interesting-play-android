package com.xtguiyi.play.ui.videoPlayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.play.databinding.ItemVideoCard2Binding
import com.xtguiyi.play.model.VideoInfoModel
import com.xtguiyi.play.utils.CommonUtil
import com.xtguiyi.play.utils.GlideUtil
import com.xtguiyi.play.utils.TimeUtil

class RelateVideoCardAdapter(private val videoData: MutableList<VideoInfoModel>): RecyclerView.Adapter<RelateVideoCardAdapter.ViewHolder>() {
    private var mClickCall: ((view: View,position: Int, videoData: VideoInfoModel) -> Unit)? = null

    public fun setOnClickListenerByRoot(mClickCall: (view: View,position: Int, videoData: VideoInfoModel) -> Unit) {
        this.mClickCall = mClickCall
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoCard2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(videoData[position])
        holder.binding.root.setOnClickListener {
            mClickCall?.invoke(it, position, videoData[position])
        }
    }

    override fun getItemCount(): Int {
        return videoData.size
    }

    class ViewHolder(val binding: ItemVideoCard2Binding):RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: VideoInfoModel){
            binding.title.text = data.title
            binding.author.text = data.owner.name
            binding.duration.text = TimeUtil.geDurationTime(data.duration)
            binding.views.text = CommonUtil.formatNumber(data.views)
            GlideUtil.loadUrl(data.pic, binding.pic.context, binding.pic)
        }
    }


    fun addItems(newItems: List<VideoInfoModel>) {
        val startPosition = videoData.size
        videoData.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}