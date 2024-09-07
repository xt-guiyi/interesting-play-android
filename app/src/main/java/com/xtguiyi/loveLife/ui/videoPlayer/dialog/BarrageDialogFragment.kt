package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogBarrageBinding
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.launch


class BarrageDialogFragment(private val barrageInfo: BarrageInfo) : DialogFragment() {
    private lateinit var binding: DialogBarrageBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var imeVisible = false
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
        binding = DialogBarrageBinding.inflate(inflater, container, false)
        binding.barrageAction.layoutParams.height =
            (DisplayUtil.getScreenHeight() * 0.4).toInt()
        binding.barrageAction.requestLayout()
        configuration()
        initView()
        bindingListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 聚焦，显示软键盘
        binding.inputView.requestFocus()
        updateActionToggle(true)
        updateImeToggle(true)
    }

    private fun configuration() {
        // 配置窗口变化
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            if(!isResumed) return@setOnApplyWindowInsetsListener insets // 切后台，不应修改状态
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBar = insets.getInsets(WindowInsetsCompat.Type.ime())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            v.setPadding(0, 0, 0, systemBars.bottom)
            if (!imeVisible && isAdded) {
                updateActionToggle(false)
            } else if (imeVisible && isAdded) {
                updateActionToggle(true)
            }
            // 如果软键盘弹出，且软键盘高度小于当前屏幕高度*0.3，这是为了避免软键盘高度太小
            if (imeVisible && imeBar.bottom > (DisplayUtil.getScreenHeight() * 0.3).toInt()) {
                binding.barrageAction.layoutParams.height = imeBar.bottom - systemBars.bottom + 30
                binding.barrageAction.requestLayout()
            }
            insets
        }

        isCancelable = true // 点击外部是否可取消
        // 配置window属性,可覆盖xml定义
        dialog?.window?.apply {
            windowInsetsController = WindowCompat.getInsetsController(this,decorView)
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = ResourcesCompat.getColor(resources, R.color.green_100, null)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.BOTTOM)
            // decorView不适配系统栏区域，可以衍生到系统栏
            WindowCompat.setDecorFitsSystemWindows(this, false)

        }
    }

    // 初始化View
    private fun initView() {
        // 这里用编程方式加入字体大小，弹幕位置view
        val color = ContextCompat.getColorStateList(requireContext(), R.color.font_color)
        fontList.forEachIndexed { _, item ->
            // drawable必须每次循环都创建一个新的引用
            val drawableLeft =
                ResourcesCompat.getDrawable(resources, R.drawable.font_size_icon, null)
            drawableLeft?.setTintList(color)
            binding.fontRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                // 设置drawable
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(2f))
                gravity = Gravity.CENTER
                // 设置margin
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(25f),
                    0
                )
                layoutParams = lp
            })
        }

        positionList.forEachIndexed { _, item ->
            val drawableLeft =
                ResourcesCompat.getDrawable(resources, R.drawable.barrage_position_icon, null)
            drawableLeft?.setTintList(color)
            binding.positionRadioGroup.addView(TextView(requireContext()).apply {
                text = item
                setTextColor(color)
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                setCompoundDrawablePadding(DisplayUtil.dip2px(2f))
                gravity = Gravity.CENTER
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    0,
                    0,
                    DisplayUtil.dip2px(25f),
                    0
                )
                layoutParams = lp
            })
        }
        // 设置选中项
        binding.fontRadioGroup.setSelected(fontList.indexOf(barrageInfo.size))
        binding.positionRadioGroup.setSelected(positionList.indexOf(barrageInfo.position))
        binding.colorRadioGroup.setSelected(colorList.indexOf(barrageInfo.color))
    }

    // 监听事件
    @SuppressLint("ClickableViewAccessibility")
    private fun bindingListener() {
        binding.barrageDialog.setOnTouchListener { _, event ->
            val childRect = Rect()
            binding.barrageBox.getHitRect(childRect)
            if (!childRect.contains(event.x.toInt(), event.y.toInt())) {
                 dismiss()
            }
            return@setOnTouchListener true
        }
        // 发送消息事件
        binding.send.setOnClickListener {
            if (binding.inputView.text?.length == 0) return@setOnClickListener
            if (requireActivity() is OnBarrageListener) {
                lifecycleScope.launch {
                    barrageInfo.message = binding.inputView.text.toString()
                    if ((requireActivity() as OnBarrageListener).sendBarrage(barrageInfo)) {
                        binding.inputView.text = null
                        windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                        dismiss()
                    }
                }
            }
        }
        // 输入法发送事件
        binding.inputView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // 处理发送事件
                if (requireActivity() is OnBarrageListener) {
                    lifecycleScope.launch {
                        barrageInfo.message = binding.inputView.text.toString()
                        if ((requireActivity() as OnBarrageListener).sendBarrage(barrageInfo)) {
                            binding.inputView.text = null
                            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                            dismiss()
                        }
                    }
                }
            }
            return@setOnEditorActionListener true
        }
        // 文字变化事件
        binding.inputView.doOnTextChanged { text, _, _, _ ->
            if (text?.isNotEmpty() == true) {
                binding.send.backgroundTintList =
                    resources.getColorStateList(R.color.green_300, null)
            } else {
                binding.send.backgroundTintList =
                    resources.getColorStateList(R.color.sliver_400, null)
            }
        }
        // 显示隐藏软键盘
        binding.actionToggle.setOnClickListener {
            val status = !imeVisible
            updateActionToggle(status)
            updateImeToggle(status)
        }
        // 监听单选事件
        binding.fontRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.size = fontList[index]
        }
        binding.positionRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.position = positionList[index]
        }
        binding.colorRadioGroup.setOnSelectedChangeListener { index, _ ->
            barrageInfo.color = colorList[index]
        }
    }

    private fun updateActionToggle(status: Boolean) {
        imeVisible = status
        binding.actionToggle.backgroundTintList =
            resources.getColorStateList(if (status) R.color.sliver_400 else R.color.green_300, null)
    }

    private fun updateImeToggle(status: Boolean) {
        imeVisible = status
        if (imeVisible) {
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

    companion object {
        const val TAG = "BarrageDialogFragment"
    }
}