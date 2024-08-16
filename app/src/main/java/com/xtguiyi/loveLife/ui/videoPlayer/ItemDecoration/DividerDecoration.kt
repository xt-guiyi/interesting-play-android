package com.xtguiyi.loveLife.ui.videoPlayer.ItemDecoration

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.RelateVideoCardAdapter

class DividerDecoration(ctx: Context, direction: Int): MaterialDividerItemDecoration(ctx, direction) {
    override fun shouldDrawDivider(position: Int, adapter: RecyclerView.Adapter<*>?): Boolean {
        adapter?.let {
            return (position != 0 && position != adapter.itemCount - 1 &&  position != adapter.itemCount - 2)
        }
        return false
    }
}