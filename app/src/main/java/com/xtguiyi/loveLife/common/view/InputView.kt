package com.xtguiyi.loveLife.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.WindowInsetsAnimation.Bounds
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.utils.DisplayUtil

class InputView(private val ctx: Context, private val attr: AttributeSet): AppCompatEditText(ctx, attr) {

    private val mDeleteDrawable = ResourcesCompat.getDrawable(resources, R.drawable.delete, null)
    private val deletePaddingEnd = DisplayUtil.dip2px(ctx, 6f) // 与右边距离
    private var isShowDeleteDrawable = false

    init {
        // 文本变化监听
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setDeleteDrawableVisible(!s.isNullOrEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * 设置删除图标状态
     * @param visibility 可见状态
     * */
    fun setDeleteDrawableVisible(visibility: Boolean) {
        isShowDeleteDrawable = visibility
    }

    /**
     * 获取删除图标位置
     * */
    private fun getDeleteBounds(computedOffsetIfNeed: Boolean): Rect {
        if(mDeleteDrawable != null) {
            val left = width - mDeleteDrawable.intrinsicWidth - deletePaddingEnd +  if(computedOffsetIfNeed) computeHorizontalScrollOffset() else 0 // X 坐标
            val top = (height - mDeleteDrawable.intrinsicHeight) / 2 // Y 坐标
            val right = width - deletePaddingEnd  + if(computedOffsetIfNeed) computeHorizontalScrollOffset() else 0 // 右边界 = 宽度 - 边距 - 滚动偏移量
            val bottom = (height - mDeleteDrawable.intrinsicHeight) / 2 + mDeleteDrawable.intrinsicHeight
            return Rect(left,top, right, bottom)
        }
        return Rect()
    }

    /**
     * 删除图标点击事件
     * */
    private fun onDeleteDrawableClick() {
        // 重置 EditText 的文字
        text = null
        setDeleteDrawableVisible(false);
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (isShowDeleteDrawable) {
                val rect = getDeleteBounds(false)
                // 检查点击位置是否在 Drawable 的边界内
                if (event.x >= rect.left && event.x <= rect.right && event.y >= rect.top && event.y <= rect.bottom) {
                    // 处理点击事件
                    onDeleteDrawableClick()
                    return true // 事件已处理
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isShowDeleteDrawable && mDeleteDrawable != null) {
            mDeleteDrawable.bounds = getDeleteBounds(true)
            mDeleteDrawable.draw(canvas)
        }
    }
}