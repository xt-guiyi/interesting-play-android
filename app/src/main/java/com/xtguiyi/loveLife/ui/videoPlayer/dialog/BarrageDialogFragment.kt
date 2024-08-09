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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import kotlinx.coroutines.launch


class BarrageDialogFragment: DialogFragment() {
    private lateinit var mInputView: EditText
    private lateinit var mSendButton: View
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
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
        bindingListener()
    }

    override fun onStart() {
        super.onStart()
        // 聚焦，显示软键盘
        dialog?.window?.let {
            mInputView.requestFocus()
            windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
            windowInsetsController.show(WindowInsetsCompat.Type.ime())
        }

        // 定义自定义window属性,可覆盖xml定义
        dialog?.window?.apply {
            // 适配系统窗口，实现沉侵布局
            WindowCompat.setDecorFitsSystemWindows(this, false)
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    // 监听事件
    private fun bindingListener() {
        mSendButton.setOnClickListener {
            if(mInputView.text.isEmpty()) return@setOnClickListener
            if(requireActivity() is OnBarrageListener) {
                lifecycleScope.launch {
                    if((requireActivity() as OnBarrageListener).sendBarrage(mInputView.text.toString())) {
                        windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                        dismiss()
                    }
                }
            }
        }

        mInputView.doOnTextChanged { text, start, before, count ->
            if(text?.isNotEmpty() == true) {
                mSendButton.backgroundTintList = resources.getColorStateList(R.color.green_300, null)
            }else {
                mSendButton.backgroundTintList = resources.getColorStateList(R.color.sliver_400, null)
            }
        }
        mInputView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 处理发送事件
                if(requireActivity() is OnBarrageListener) {
                    lifecycleScope.launch {
                        if((requireActivity() as OnBarrageListener).sendBarrage(mInputView.text.toString())) {
                            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                            dismiss()
                        }
                    }
                }
            }
            return@setOnEditorActionListener true
        }
    }

    interface OnBarrageListener {
        // 发送弹幕
        suspend fun sendBarrage(message: String): Boolean
    }
}