package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogCommentBinding

class CommentDialogFragment : DialogFragment() {
    private lateinit var binding: DialogCommentBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var isScale = false

    init {
        setStyle(STYLE_NORMAL, R.style.CustomDialog2)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommentBinding.inflate(inflater, container, false)
        configuration()
        initView()
        bindingListener()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        binding.commentInput.clearFocus()
    }

    private fun configuration() {
        // 聚焦，显示软键盘
        dialog?.window?.let {
            windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
            binding.commentInput.requestFocus()
            // 解决低版本dialogFragment弹出软键盘无效，加300ms延迟
            binding.commentInput.postDelayed({
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }, 300)
        }
        dialog?.window?.apply {
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = ResourcesCompat.getColor(resources,R.color.green_100, null)
            windowInsetsController.isAppearanceLightNavigationBars = true
            windowInsetsController.isAppearanceLightStatusBars = false
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // decorView不适配系统栏区域，可以衍生到系统栏
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
    }

    fun initView() {}

    fun bindingListener() {
        binding.commentDialog.setOnClickListener {
            if(!isScale) dismiss()
        }
        binding.scaleView.setOnClickListener {
            if (isScale) {
                binding.commentWrapper.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.commentInput.maxLines = 6
                binding.commentInput.minLines = 2
            } else {
               binding.commentWrapper.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.commentInput.maxLines = Int.MAX_VALUE
                binding.commentInput.minLines = 3
            }
            isScale = !isScale
        }
    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }
}