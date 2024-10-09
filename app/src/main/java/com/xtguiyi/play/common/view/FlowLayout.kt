package com.xtguiyi.play.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

/**
 * 流式布局
 * */
open class FlowLayout : ViewGroup {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 父容器生成 子view 的布局LayoutParams;
     * 一句话道出LayoutParams的本质：LayoutParams是Layout提供给其中的Children使用的。
     * 如果要自定义ViewGroup支持子控件的layout_margin参数，则自定义的ViewGroup类必须重载generateLayoutParams()函数，
     * 并且在该函数中返回一个ViewGroup.MarginLayoutParams派生类对象，这样才能使用margin参数。
     */
    override fun generateLayoutParams(
        p: LayoutParams
    ): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        val measureWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureHeightMode = MeasureSpec.getMode(heightMeasureSpec)


        var lineWidth = 0
        var lineHeight = 0
        var height = 0
        var width = 0
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            //如果忘记重写generateLayoutParams，则hild.getLayoutParams()将不是MarginLayoutParams的实例
            //在强制转换时就会出错，此时我们把左右间距设置为0，但由于在计算布局宽高时没有加上间距值，就是计算出的宽高要比实际小，所以是onLayout时就会出错
            var lp = child.layoutParams
            var childWidth = 0
            var childHeight = 0
            if(lp is MarginLayoutParams) {
                childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
                childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            }else {
                 childWidth = child.measuredWidth
                 childHeight = child.measuredHeight
            }

            if (lineWidth + childWidth > measureWidth) {
                //需要换行
                width = max(lineWidth.toDouble(), width.toDouble()).toInt()
                height += lineHeight
                //因为由于盛不下当前控件，而将此控件调到下一行，所以将此控件的高度和宽度初始化给lineHeight、lineWidth
                lineHeight = childHeight
                lineWidth = childWidth
            } else {
                // 否则累加值lineWidth,lineHeight取最大高度
                lineHeight = max(lineHeight.toDouble(), childHeight.toDouble()).toInt()
                lineWidth += childWidth
            }

            //最后一行是不会超出width范围的，所以要单独处理
            if (i == count - 1) {
                height += lineHeight
                width = max(width.toDouble(), lineWidth.toDouble()).toInt()
            }
        }
        //当属性是MeasureSpec.EXACTLY时，那么它的高度就是确定的，
        // 只有当是wrap_content时，根据内部控件的大小来确定它的大小时，大小是不确定的，属性是AT_MOST,此时，就需要我们自己计算它的应当的大小，并设置进去
        setMeasuredDimension(
            if ((measureWidthMode == MeasureSpec.EXACTLY)) measureWidth
            else width, if ((measureHeightMode == MeasureSpec.EXACTLY)) measureHeight
            else height
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var lineWidth = 0
        var lineHeight = 0
        var top = 0
        var left = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            val lp = child
                .layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (childWidth + lineWidth > measuredWidth) {
                //如果换行,当前控件将跑到下一行，从最左边开始，所以left就是0，而top则需要加上上一行的行高，才是这个控件的top点;
                top += lineHeight
                left = 0
                //同样，重新初始化lineHeight和lineWidth
                lineHeight = childHeight
                lineWidth = childWidth
            } else {
                lineHeight = max(lineHeight.toDouble(), childHeight.toDouble()).toInt()
                lineWidth += childWidth
            }
            //计算childView的left,top,right,bottom
            val lc = left + lp.leftMargin
            val tc = top + lp.topMargin
            val rc = lc + child.measuredWidth
            val bc = tc + child.measuredHeight
            child.layout(lc, tc, rc, bc)
            //将left置为下一子控件的起始点
            left += childWidth
        }
    }
}
