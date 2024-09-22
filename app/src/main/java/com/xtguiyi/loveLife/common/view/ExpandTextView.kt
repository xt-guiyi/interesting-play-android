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
import com.xtguiyi.loveLife.R

class ExpandTextView: AppCompatTextView {

    private var expandable = false
    private var expandSuffixText = ""
    private var collapseSuffixText = ""
    private var lines = 0
    private var initialText = ""
    private var issetup = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attribute: AttributeSet) : super(context, attribute) {
        initAttr(context, attribute)
    }
    constructor(context: Context, attribute: AttributeSet, defStyleAttr: Int) : super(context, attribute, defStyleAttr) {
        initAttr(context, attribute)
    }


    fun initAttr(context: Context, attribute: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attribute, R.styleable.ExpandTextView)
        expandSuffixText = attrs.getString(R.styleable.ExpandTextView_expandSuffixText) ?: "展开"
        collapseSuffixText = attrs.getString(R.styleable.ExpandTextView_collapseSuffixText) ?: "收起"
        lines = attrs.getInt(R.styleable.ExpandTextView_lines, 0)
        expandable = attrs.getBoolean(R.styleable.ExpandTextView_expandable, false)
        attrs.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(initialText.isEmpty()) initialText = text.toString()
        highlightColor = Color.TRANSPARENT
        movementMethod = LinkMovementMethod.getInstance()
        if(!issetup && lines > 0) setup()
    }


    fun setup() {
        val layout = StaticLayout.Builder.obtain(initialText, 0, initialText.length, paint, measuredWidth - paddingLeft - paddingRight).build()
        if (layout.lineCount > lines) {
            val endIndex = layout.getLineEnd(lines - 1)
            // 收起文本
            val collapseText = SpannableStringBuilder("${initialText.substring(0, endIndex - expandSuffixText.length -1)}...$expandSuffixText")
            // 展开文本
            val expandText = SpannableStringBuilder("$initialText  $collapseSuffixText")
            collapseText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        expandable = true
                        issetup = true
                        mClickCall?.invoke(true)
                        maxLines = Int.MAX_VALUE
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
            expandText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        expandable = false
                        issetup = true
                        mClickCall?.invoke(false)
                        maxLines = lines
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
            maxLines = lines
            text = if(expandable) expandText else collapseText
        }

    }

    private var mClickCall: ((expandable: Boolean) -> Unit)? = null

    fun setOnClickListenerExpandChange(mClickCall: (expandable: Boolean) -> Unit) {
        this.mClickCall = mClickCall
    }
}