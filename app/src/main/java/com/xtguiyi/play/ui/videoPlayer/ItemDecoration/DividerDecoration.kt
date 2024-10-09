package com.xtguiyi.play.ui.videoPlayer.ItemDecoration

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration

class DividerDecoration(ctx: Context, direction: Int): MaterialDividerItemDecoration(ctx, direction) {
    override fun shouldDrawDivider(position: Int, adapter: RecyclerView.Adapter<*>?): Boolean {
        adapter?.let {
            return (position != 0 && position != adapter.itemCount - 1 &&  position != adapter.itemCount - 2)
        }
        return false
    }
}