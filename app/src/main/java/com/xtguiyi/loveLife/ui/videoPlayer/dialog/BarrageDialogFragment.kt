package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.common.view.RadioGroup
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BarrageDialogFragment(val barrageInfo: BarrageInfo) : DialogFragment() {
    private lateinit var mInputView: EditText
    private lateinit var mSendButton: View
    private lateinit var mBarrageAction: View
    private lateinit var mImeToggle: View
    private lateinit var mFontRadioGroup: RadioGroup
    private lateinit var mPositionRadioGroup: RadioGroup
    private lateinit var mColorRadioGroup: RadioGroup
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private val fontList = listOf("默认", "较小")
    private val positionList = listOf("滚动", "置顶", "置底")
    private val colorList = listOf(
        "#FFFFFFFF",
        "#fc8bab",
        "#FE3737",
        "#FF8B00",
        "#04B578",
        "#00E5FF",
        "#D500F9",
        "#76FF03",
        "#1DE9B6",
        "#0D2780",
        "#DBB22F"
    )

    init {
        // 设置dialog通用样式，需要在onCreateDialog方法被调用前设置
        setStyle(STYLE_NORMAL, R.style.CustomDialog1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.barrage_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInputView = view.findViewById(R.id.inputView)
        mSendButton = view.findViewById(R.id.send)
        mImeToggle = view.findViewById(R.id.ime_toggle)
        mFontRadioGroup = view.findViewById(R.id.font_radioGroup)
        mPositionRadioGroup = view.findViewById(R.id.position_radioGroup)
        mColorRadioGroup = view.findViewById(R.id.color_radioGroup)
        mBarrageAction = view.findViewById(R.id.barrage_action)
        mBarrageAction.layoutParams.height =
            (DisplayUtil.getScreenHeight(requireContext()) * 0.34).toInt()
        initView()
        bindingListener()
    }

    override fun onStart() {
        super.onStart()
        // 聚焦，显示软键盘
        dialog?.window?.let {
            windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
            mInputView.requestFocus()
            lifecycleScope.launch {
                delay(100)
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }
        }

        // 定义自定义window属性,可覆盖xml定义
        dialog?.window?.apply {
            // 适配系统窗口，实现沉侵布局
            WindowCompat.setDecorFitsSystemWindows(this, false)
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            navigationBarColor = resources.getColor(R.color.green_100, null)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    // 初始化状态
    private fun initView() {
        val color = ContextCompat.getColorStateList(requireContext(), R.color.font_color)
        fontList.forEachIndexed { _, item ->
            val drawableLeft = ResourcesCompat.getDrawable(resources, R.drawable.a_1, null)
            drawableLeft?.setTintList(color)
            mFontRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                // 设置drawable
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(requireContext(), 6f))
                gravity = Gravity.CENTER
                // 设置margin
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(requireContext(), 30f),
                    0
                )
                layoutParams = lp
            })
        }
        mFontRadioGroup.setSelected(fontList.indexOf(barrageInfo.size))

        positionList.forEachIndexed { index, item ->
            val drawableLeft = ResourcesCompat.getDrawable(resources, R.drawable.a_1, null)
            drawableLeft?.setTintList(color)
            mPositionRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(requireContext(), 6f))
                gravity = Gravity.CENTER
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(requireContext(), 30f),
                    0
                )
                layoutParams = lp
            })
        }
        mPositionRadioGroup.setSelected(positionList.indexOf(barrageInfo.position))
        colorList.forEachIndexed { index, item ->
            val drawableLeft = ResourcesCompat.getDrawable(resources, R.drawable.a_1, null)
            mColorRadioGroup.addView(TextView(requireContext()).apply {
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawableLeft)
                setCompoundDrawablePadding(DisplayUtil.dip2px(requireContext(), -6f))


                background = ResourcesCompat.getDrawable(resources, R.drawable.round_6, null)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor(item))
                gravity = Gravity.CENTER
                val lp = ViewGroup.MarginLayoutParams(
                    DisplayUtil.dip2px(requireContext(), 26f),
                    DisplayUtil.dip2px(requireContext(), 20f)
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(requireContext(), 24f),
                    DisplayUtil.dip2px(requireContext(), 24f)
                )
                layoutParams = lp
            })
        }
        mColorRadioGroup.setSelected(colorList.indexOf(barrageInfo.color))
    }

    // 监听事件
    private fun bindingListener() {
        mSendButton.setOnClickListener {
            if(mInputView.text.isEmpty()) return@setOnClickListener
            if(requireActivity() is OnBarrageListener) {
                lifecycleScope.launch {
                    barrageInfo.message = mInputView.text.toString()
                    if ((requireActivity() as OnBarrageListener).sendBarrage(barrageInfo)) {
                        windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                        dismiss()
                    }
                }
            }
        }
        // 监听文字变化事件
        mInputView.doOnTextChanged { text, start, before, count ->
            if(text?.isNotEmpty() == true) {
                mSendButton.backgroundTintList = resources.getColorStateList(R.color.green_300, null)
            }else {
                mSendButton.backgroundTintList = resources.getColorStateList(R.color.sliver_400, null)
            }
        }
        // 监听输入法发送事件
        mInputView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 处理发送事件
                if(requireActivity() is OnBarrageListener) {
                    lifecycleScope.launch {
                        barrageInfo.message = mInputView.text.toString()
                        if ((requireActivity() as OnBarrageListener).sendBarrage(barrageInfo)) {
                            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                            dismiss()
                        }
                    }
                }
            }
            return@setOnEditorActionListener true
        }
        // 显示隐藏软键盘
        var imeStatus = true
        mImeToggle.setOnClickListener {
            if (imeStatus) {
                mImeToggle.backgroundTintList = resources.getColorStateList(R.color.green_300, null)
                windowInsetsController.hide(WindowInsetsCompat.Type.ime())
            } else {
                mImeToggle.backgroundTintList =
                    resources.getColorStateList(R.color.sliver_400, null)
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }
            imeStatus = !imeStatus
        }

        // 监听单选事件
        mFontRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.size = fontList[index]
        }

        mPositionRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.position = positionList[index]
        }

        mColorRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.color = colorList[index]
        }

    }

    /**
     * 弹幕信息
     * @param message 弹幕信息
     * @param size 弹幕大小:默认,较小
     * @param position 弹幕位置:滚动,置顶,置底
     * @param color 颜色值
     * */
    data class BarrageInfo(
        var message: String,
        var size: String,
        var position: String,
        var color: String
    )

    interface OnBarrageListener {
        // 发送弹幕
        suspend fun sendBarrage(barrageInfo: BarrageInfo): Boolean
    }
}