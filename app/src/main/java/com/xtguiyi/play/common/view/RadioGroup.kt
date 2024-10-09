package com.xtguiyi.play.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.children

/**
 * 单选group
 * 其实单选实现逻辑都是一样的，区别在于布局方式，我这里使用的流式布局，如果需要其他布局方式，那么继承其他布局就可
 * */
class RadioGroup : FlowLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mLastSelected = 0
    private var mSelected = 0
    private var selectedListener: OnSelectedChangeListener? = null

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 确保所有子视图已经完全加载和布局
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                // 执行需要在子视图完全加载后执行的操作
                children.forEachIndexed { index, view ->
                    view.setOnClickListener {
                        mLastSelected = mSelected
                        mSelected = index
                        updateSelected()
                        selectedListener?.onItemSelectedChange(index, view)
                    }
                }
            }
        })
    }

    private fun updateSelected() {
        getChildAt(mLastSelected).isSelected = false
        getChildAt(mSelected).isSelected = true
    }

    // 设置选中项
    fun setSelected(index: Int) {
        mLastSelected = mSelected
        mSelected = index
        getChildAt(mLastSelected).isSelected = false
        getChildAt(mSelected).isSelected = true
    }

    fun setOnSelectedChangeListener(listener: OnSelectedChangeListener) {
        selectedListener = listener
    }

    fun interface OnSelectedChangeListener {
        fun onItemSelectedChange(index: Int, view: View)
    }
}