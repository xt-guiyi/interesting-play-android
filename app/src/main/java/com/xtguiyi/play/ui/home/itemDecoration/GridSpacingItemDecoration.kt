package com.xtguiyi.play.ui.home.itemDecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val spanSize =
            (parent.layoutManager as GridLayoutManager).spanSizeLookup.getSpanSize(position)
        outRect.bottom = spacing / 2
        if (spanSize == spanCount) return
        var column = 0
        for (i in 0 until position) {
            column += (parent.layoutManager as GridLayoutManager).spanSizeLookup.getSpanSize(i)
        }
        column = column % spanCount
        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
    }
}