package com.xtguiyi.loveLife.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.children

class RadioGroup:FlowLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mSelected = 0
    private var selectedListener: OnSelectedChangeListener? = null

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 确保所有子视图已经完全加载和布局
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                // 执行需要在子视图完全加载后执行的操作
//                getChildAt(mSelected).isSelected = true
                children.forEachIndexed { index, view ->
                    view.setOnClickListener {
                        mSelected = index
                        updateSelected()
                        selectedListener?.onItemSelectedChange(index,view)
                    }
                }
            }
        })
    }

    private fun updateSelected() {
        children.forEachIndexed { index, view ->
            view.isSelected = index == mSelected
        }
    }

    fun setSelected(index: Int) {
        mSelected = index
        getChildAt(mSelected).isSelected = true
    }

    fun setOnSelectedChangeListener(listener: OnSelectedChangeListener) {
        selectedListener = listener
    }

    fun interface OnSelectedChangeListener {
        fun onItemSelectedChange(index: Int, view: View)
    }
}