package com.xtguiyi.loveLife.common.ItemDecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hjq.toast.Toaster

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = if(column == 0) 0 else 30
//            outRect.right =  if(column == 0) 30 else 0
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
            Toaster.show("$position:$column---${column * spacing / spanCount} --- ${spacing - (column + 1) * spacing / spanCount}")
        }
    }
}