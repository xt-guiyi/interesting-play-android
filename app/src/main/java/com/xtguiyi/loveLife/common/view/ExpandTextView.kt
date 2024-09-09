package com.xtguiyi.loveLife.common.view

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.hjq.toast.Toaster

class ExpandTextView: AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attribute: AttributeSet) : super(context, attribute) {
        initAttr(context, attribute)
    }
    constructor(context: Context, attribute: AttributeSet, defStyleAttr: Int) : super(context, attribute, defStyleAttr) {
        initAttr(context, attribute)
    }


    fun initAttr(context: Context, set: AttributeSet) {}

    private lateinit var expandText: SpannableStringBuilder
    private lateinit var collapseText: SpannableStringBuilder
    private var isExpand = false
    private val expandSuffixText = "展开"
    private val collapseSuffixText = "收起"
    private var initialMaxLines = 0
    private var initialText = ""

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
        if(initialText.length == 0)initialText = text.toString()
        initialMaxLines = 3
        maxLines = Int.MAX_VALUE
        if(!isExpand)setupText()
    }


    fun setupText() {
        val layout = StaticLayout.Builder.obtain(initialText, 0, initialText.length, paint, measuredWidth - paddingLeft - paddingRight).build()
        Toaster.show("$isExpand-${layout.lineCount} -- $initialMaxLines")
        if (layout.lineCount > initialMaxLines) {
            // 收起文本
            val endIndex = layout.getLineEnd(initialMaxLines - 1)
            collapseText = SpannableStringBuilder("${initialText.substring(0, endIndex - expandSuffixText.length -1)}...$expandSuffixText")
            collapseText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        isExpand = true
                        mClickCall?.invoke(true)
                        text = expandText

                    }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false // 如果需要下划线，可以设置 true
                        ds.color = Color.parseColor("#1989fa")     // 设置链接文本的颜色
                    }
                },
                collapseText.length - expandSuffixText.length - 3, // start
                collapseText.length, // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 展开文本
            expandText = SpannableStringBuilder("$initialText  $collapseSuffixText")
            expandText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        isExpand = false
                        mClickCall?.invoke(false)
                        text = collapseText
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false // 如果需要下划线，可以设置 true
                        ds.color = Color.parseColor("#1989fa")        // 设置链接文本的颜色
                    }
                },
                expandText.length - collapseSuffixText.length, // start
                expandText.length, // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = collapseText
        }

    }

    private var mClickCall: ((isExpand: Boolean) -> Unit)? = null

    public fun setOnClickListenerExpandChange(mClickCall: (isExpand: Boolean) -> Unit) {
        this.mClickCall = mClickCall
    }
}