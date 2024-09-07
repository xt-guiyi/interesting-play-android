package com.xtguiyi.loveLife.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.ItemFooterItemBinding

/**
 * recycleView 底部加载适配器1
 * */
class FooterAdapter(private val retry: () -> Unit) : RecyclerView.Adapter<FooterAdapter.ViewHolder>() {

    private var mLoadResult: LoadResult = LoadResult.NotLoading()

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFooterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bingTo(mLoadResult)
    }


    class ViewHolder(val binding: ItemFooterItemBinding, private val retry: () -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bingTo(loadResult: LoadResult) {
            binding.progressBar.isVisible = loadResult is LoadResult.Loading
            binding.retryAction.isVisible = loadResult is LoadResult.Error
            binding.retryAction.setOnClickListener {
                retry.invoke()
            }
            binding.noMoreTips.isVisible = loadResult is LoadResult.NotMore
        }
    }

    fun setStatus(loadResult: LoadResult) {
        mLoadResult = loadResult
        notifyItemChanged(0)
    }

    fun getStatus(): LoadResult {
       return  mLoadResult
    }

    sealed class LoadResult {
        data class Loading(val message: String? = "加载中") : LoadResult()
        data class NotLoading(val message: String? = "未加载") : LoadResult()
        data class Error(val message: String? = "加载失败") : LoadResult()
        data class NotMore(val message: String? = "没有跟多了") : LoadResult()
    }

}