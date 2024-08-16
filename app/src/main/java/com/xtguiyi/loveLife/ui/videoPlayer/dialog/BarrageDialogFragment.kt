package com.xtguiyi.loveLife.ui.videoPlayer.dialog

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
import kotlinx.coroutines.launch


class BarrageDialogFragment(private val barrageInfo: BarrageInfo) : DialogFragment() {
    private lateinit var mInputView: EditText
    private lateinit var mSendButton: View
    private lateinit var mBarrageAction: View
    private lateinit var mActionToggle: View
    private lateinit var mFontRadioGroup: RadioGroup
    private lateinit var mPositionRadioGroup: RadioGroup
    private lateinit var mColorRadioGroup: RadioGroup
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var imeStatus = false
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
        return inflater.inflate(R.layout.barrage_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInputView = view.findViewById(R.id.inputView)
        mSendButton = view.findViewById(R.id.send)
        mActionToggle = view.findViewById(R.id.action_toggle)
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
        isCancelable = true // 点击外部是否可取消
        // 聚焦，显示软键盘
        dialog?.window?.let {
            windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
            mInputView.requestFocus()
            // 解决低版本dialogFragment弹出软键盘无效，加300ms延迟
            mInputView.postDelayed({
                updateActionToggle(true)
                updateImeToggle(true)
            }, 300)
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

    // 初始化View
    private fun initView() {
        // 这里用编程方式加入view
        val color = ContextCompat.getColorStateList(requireContext(), R.color.font_color)
        fontList.forEachIndexed { _, item ->
            // drawable必须每次循环都创建一个新的引用
            val drawableLeft = ResourcesCompat.getDrawable(resources, R.drawable.font_size_icon, null)
            drawableLeft?.setTintList(color)
            mFontRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                // 设置drawable
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(requireContext(), 2f))
                gravity = Gravity.CENTER
                // 设置margin
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(requireContext(), 25f),
                    0
                )
                layoutParams = lp
            })
        }

        positionList.forEachIndexed { _, item ->
            val drawableLeft = ResourcesCompat.getDrawable(resources, R.drawable.barrage_position_icon, null)
            drawableLeft?.setTintList(color)
            mPositionRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(requireContext(), 2f))
                gravity = Gravity.CENTER
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(requireContext(), 25f),
                    0
                )
                layoutParams = lp
            })
        }
        // 设置选中项
        mFontRadioGroup.setSelected(fontList.indexOf(barrageInfo.size))
        mPositionRadioGroup.setSelected(positionList.indexOf(barrageInfo.position))
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
                        mInputView.text = null
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
                            mInputView.text = null
                            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                            dismiss()
                        }
                    }
                }
            }
            return@setOnEditorActionListener true
        }
        // 显示隐藏软键盘
        mActionToggle.setOnClickListener {
            val status = !imeStatus
            updateActionToggle(status)
            updateImeToggle(status)
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

     fun updateActionToggle(status: Boolean){
        imeStatus = status
        mActionToggle.backgroundTintList =
            resources.getColorStateList(if(status) R.color.sliver_400 else R.color.green_300, null)
    }

    private fun updateImeToggle(status: Boolean) {
        imeStatus = status
        if (imeStatus) {
            windowInsetsController.show(WindowInsetsCompat.Type.ime())
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
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