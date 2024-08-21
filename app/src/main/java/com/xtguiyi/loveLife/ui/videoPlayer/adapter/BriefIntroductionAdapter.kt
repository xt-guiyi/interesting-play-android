package com.xtguiyi.loveLife.ui.videoPlayer.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.LayoutBriefIntroductionCardBinding
import com.xtguiyi.loveLife.entity.VideoInfo
import com.xtguiyi.loveLife.utils.CommonUtil
import com.xtguiyi.loveLife.utils.DisplayUtil
import com.xtguiyi.loveLife.utils.GlideUtil
import com.xtguiyi.loveLife.utils.TimeUtil

class BriefIntroductionAdapter(private var list: List<VideoInfo>): RecyclerView.Adapter<BriefIntroductionAdapter.ViewHolder>() {

    private var isExpand = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutBriefIntroductionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindTo(list[position])
        holder.binding.expandedArrow.setOnClickListener {
           isExpand = !isExpand
            notifyItemChanged(0,1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        changeExpandStatus(holder.binding)
    }

    class ViewHolder(val binding:LayoutBriefIntroductionCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: VideoInfo){
             binding.videoTitle.text = data.title
            binding.userName.text = data.owner.name
            binding.fans.text = String.format("%s粉", CommonUtil.formatNumber(data.owner.fans))
            binding.publishVideoNumber.text = String.format("%s视频",data.owner.videos)
            binding.videoDesc.text = data.desc
            binding.views.text = CommonUtil.formatNumber(data.views)
            binding.comment.text = CommonUtil.formatNumber(data.reply)
            binding.pubDate.text = TimeUtil.getCurrentFormattedDate(data.pubDate, "yyyy年M月dd日 HH:mm")
            binding.seeing.text = "102人正在看"
            binding.like.text = CommonUtil.formatNumber(data.like)
            binding.dislike.text = CommonUtil.formatNumber(data.dislike)
            binding.coin.text = CommonUtil.formatNumber(data.coin)
            binding.favorite.text = CommonUtil.formatNumber(data.favorite)
            binding.share.text = CommonUtil.formatNumber(data.share)
            GlideUtil.setUrlCircle(data.owner.face, binding.face.context, binding.face)

            val tagContainer = binding.tagContainer
            tagContainer.removeAllViews()
            val tags = listOf("小奶猫","小奶猫","小奶猫","小奶猫","小奶猫","小奶猫","小奶猫","小奶猫","小奶猫",)
            for (tag in tags) {
                val tagView = TextView(tagContainer.context)
                tagView.textSize = 12f
                tagView.setTextColor(tagContainer.context.getColor(R.color.sliver_400))
                tagView.background = AppCompatResources.getDrawable(tagContainer.context,R.drawable.button_bg_24)
                tagView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#dddee0"))
                // padding
                tagView.setPadding(
                    DisplayUtil.dip2px(tagContainer.context,6f),
                    DisplayUtil.dip2px(tagContainer.context,2f),
                    DisplayUtil.dip2px(tagContainer.context,6f),
                    DisplayUtil.dip2px(tagContainer.context,2f),
                )
                tagView.text = tag
                tagContainer.addView(tagView)
            }

        }
    }

    // 展开、收起详情
    private fun changeExpandStatus(binding: LayoutBriefIntroductionCardBinding) {
        if(isExpand) {
            binding.expandedArrow.rotation = 180f
            binding.videoTitle.maxLines = Int.MAX_VALUE
            binding.linearLayout.visibility = View.VISIBLE
        }else {
            binding.expandedArrow.rotation = 0f
            binding.videoTitle.maxLines = 1
            binding.linearLayout.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data:VideoInfo) {
        list = listOf(data)
        notifyDataSetChanged()
    }
}