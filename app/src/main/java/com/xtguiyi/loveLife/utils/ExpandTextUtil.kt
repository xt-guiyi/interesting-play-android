package com.xtguiyi.loveLife.utils

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
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import kotlin.text.isEmpty
import kotlin.text.substring


class ExpandTextUtil() {

    // 默认展示行数，超过多少行隐藏
    private var maxLine: Int = 3

    // 展开、收起文本颜色
    private var colorStr: String = "#0079e2"

    // 展开显示文本
    private var expandStr: String = "展开"

    // 收起显示文本
    private var foldStr: String = "收起"


    fun setMaxLine(maxLine: Int): ExpandTextUtil {
        this.maxLine = maxLine
        return this
    }


    fun setColorStr(colorStr: String): ExpandTextUtil {
        this.colorStr = colorStr
        return this
    }

    fun setFoldStr(foldStr: String): ExpandTextUtil {
        this.foldStr = foldStr
        return this
    }

    fun setExpandStr(expandStr: String): ExpandTextUtil {
        this.expandStr = expandStr
        return this
    }


    fun show(expandTextView: TextView, content: String) {
        expandTextView.post {
            // 获取TextView的画笔对象
            val paint = expandTextView.paint
            expandTextView.movementMethod = LinkMovementMethod.getInstance()
            val layout = StaticLayout.Builder.obtain(content, 0, content.length, paint, expandTextView.width - expandTextView.paddingLeft - expandTextView.paddingRight).build()
            if (layout.lineCount > maxLine) {
                val endIndex = layout.getLineEnd(maxLine - 1)
                // 收起文本
                val collapseText = SpannableStringBuilder(
                    "${
                        content.substring(
                            0,
                            endIndex - expandStr.length - 1
                        )
                    }...$expandStr"
                )
                // 展开文本
                val expandText = SpannableStringBuilder("$content $foldStr")

                collapseText.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            expandTextView.text = expandText
                            mClickCall?.invoke(true)
                        }
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false // 如果需要下划线，可以设置 true
                            ds.color = Color.parseColor(colorStr)     // 设置链接文本的颜色
                        }
                    },
                    collapseText.length - expandStr.length - 3, // start
                    collapseText.length, // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                expandText.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            expandTextView.text = collapseText
                            mClickCall?.invoke(false)
                        }
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.isUnderlineText = false // 如果需要下划线，可以设置 true
                            ds.color = Color.parseColor(colorStr)        // 设置链接文本的颜色
                        }
                    },
                    expandText.length - foldStr.length, // start
                    expandText.length, // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                expandTextView.text = collapseText
            }else {
                expandTextView.text = content
            }
        }
    }


    private var mClickCall: ((expandable: Boolean) -> Unit)? = null

    fun setOnClickListenerExpandChange(mClickCall: (expandable: Boolean) -> Unit) {
        this.mClickCall = mClickCall
    }
}