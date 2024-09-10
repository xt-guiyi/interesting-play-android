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
import com.xtguiyi.loveLife.R

class ExpandTextView: AppCompatTextView {

    private var expandable = false
    private var expandSuffixText = ""
    private var collapseSuffixText = ""
    private var lines = 0
    private var initialText = ""
    private var isBreak = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attribute: AttributeSet) : super(context, attribute) {
        initAttr(context, attribute)
    }
    constructor(context: Context, attribute: AttributeSet, defStyleAttr: Int) : super(context, attribute, defStyleAttr) {
        initAttr(context, attribute)
    }

    init {
        maxLines = Int.MAX_VALUE
        highlightColor = Color.TRANSPARENT
        movementMethod = LinkMovementMethod.getInstance()
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
        if(!isBreak && lines > 0) setup()
    }

    fun setup() {
        val layout = StaticLayout.Builder.obtain(initialText, 0, initialText.length, paint, measuredWidth - paddingLeft - paddingRight).build()
        Toaster.show("$expandable-${layout.lineCount} -- $lines")
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
                        isBreak = true
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
            expandText.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        expandable = false
                        isBreak = true
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

    private var mClickCall: ((expandable: Boolean) -> Unit)? = null

    public fun setOnClickListenerExpandChange(mClickCall: (expandable: Boolean) -> Unit) {
        this.mClickCall = mClickCall
    }
}