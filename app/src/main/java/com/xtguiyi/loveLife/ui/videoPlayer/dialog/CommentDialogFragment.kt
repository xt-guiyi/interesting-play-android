package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogCommentBinding

class CommentDialogFragment : DialogFragment() {
    private lateinit var binding: DialogCommentBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var isFullScreen = false
    private var isEmoteArea = false
    private var emoteAreaHeight = 0
    private var isClickable = false
    private var isMounted = false

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

    override fun onResume() {
        super.onResume()
        binding.commentInput.requestFocus()
        windowInsetsController.show(WindowInsetsCompat.Type.ime())
            isMounted = true

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
            // 解决低版本dialogFragment弹出软键盘无效，加500ms延迟
            binding.commentInput.postDelayed({
//                windowInsetsController.show(WindowInsetsCompat.Type.ime())
                val insets = ViewCompat.getRootWindowInsets(it.decorView) ?: return@postDelayed
                emoteAreaHeight =
                    insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - insets.getInsets(
                        WindowInsetsCompat.Type.navigationBars()
                    ).bottom
            }, 1000)

        }
        dialog?.window?.apply {
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = ResourcesCompat.getColor(resources, R.color.green_100, null)
            windowInsetsController.isAppearanceLightNavigationBars = true
            windowInsetsController.isAppearanceLightStatusBars = false
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // decorView适配系统栏区域，不可以衍生到系统栏
            WindowCompat.setDecorFitsSystemWindows(this, true)
        }
    }

    fun initView() {
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bindingListener() {
        binding.commentDialog.setOnTouchListener { _, event ->
            val childRect = Rect()
            binding.commentInputWrapper.getHitRect(childRect)
            if (!childRect.contains(event.x.toInt(), event.y.toInt())) {
                if (!isFullScreen) dismiss()
            }
            return@setOnTouchListener true
        }


        binding.scaleView.setOnClickListener {
            if (isFullScreen) {
                binding.commentInputWrapper.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT
                binding.commentInput.maxLines = 6
                binding.commentInput.minLines = 2
            } else {
                binding.commentInputWrapper.layoutParams.height =
                    ViewGroup.LayoutParams.MATCH_PARENT
                binding.commentInput.maxLines = Int.MAX_VALUE
                binding.commentInput.minLines = 3
            }
            isFullScreen = !isFullScreen
        }

        binding.emoteToggle.setOnClickListener {
            if (isClickable) return@setOnClickListener
            isClickable = true
            Handler(requireActivity().mainLooper).postDelayed({
                isClickable = false

            }, 600)
            isEmoteArea = !isEmoteArea
            if (isEmoteArea) {
                binding.emoteLayout.layoutParams.height = emoteAreaHeight
                windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                binding.emoteLayout.requestLayout()
                dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            } else {
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
                // 需要延迟，避免抖动
                binding.commentDialog.postDelayed({
//                    binding.emoteLayout.layoutParams.height = 0
//                    binding.emoteLayout.requestLayout()
//                    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }, 500)
            }
        }


        binding.commentInput.setOnClickListener { v ->
//            if (isClickable) return@setOnClickListener
//            if (isEmoteArea) {
//                isEmoteArea = false
//                windowInsetsController.show(WindowInsetsCompat.Type.ime())
//                // 需要延迟，避免抖动
//                binding.commentDialog.postDelayed({
//                    binding.emoteLayout.layoutParams.height = 0
//                    binding.emoteLayout.requestLayout()
//                    dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//                }, 300)
//            }
        }

        binding.commentDialog.viewTreeObserver.addOnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(binding.commentDialog) ?: return@addOnGlobalLayoutListener
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            Toaster.show(imeVisible)
//          if(!imeVisible && isMounted) dismiss()
        }
    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }
}