package com.xtguiyi.loveLife.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xtguiyi.loveLife.databinding.FooterItemBinding

/**
 * recycleView 底部加载适配器1
 * */
class FooterAdapter(private val retry: () -> Unit) : RecyclerView.Adapter<FooterAdapter.ViewHolder>() {

    private var mLoading = false
    private var mError = false

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FooterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bingTo(mLoading, mError)
    }


    class ViewHolder(val binding: FooterItemBinding, private val retry: () -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bingTo(loading: Boolean, error: Boolean) {
            binding.progressBar.isVisible = loading
            binding.retryAction.isVisible = error
            binding.retryAction.setOnClickListener {
                retry.invoke()
            }
        }
    }

    fun setStatus(loading: Boolean, error: Boolean) {
        mLoading = loading
        mError = error
    }

}