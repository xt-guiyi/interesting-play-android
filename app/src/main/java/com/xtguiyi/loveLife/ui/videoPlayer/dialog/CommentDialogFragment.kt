package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogCommentBinding
import com.xtguiyi.loveLife.utils.DisplayUtil

class CommentDialogFragment() : DialogFragment() {
    private lateinit var binding: DialogCommentBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var isFullScreen = false
    private var isEmoteArea = false
    private var emoteAreaHeight =  0

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
                val insets = ViewCompat.getRootWindowInsets(it.decorView) ?: return@postDelayed
                emoteAreaHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                it.decorView.setOnSystemUiVisibilityChangeListener {
                    Toaster.show("11")
                }
//                ViewCompat.setOnApplyWindowInsetsListener(it.decorView) { _, insets ->
//                    val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
//                    Toaster.show(imeVisible)
//                    null
//                }
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
                if(!isFullScreen) dismiss()
            }
            return@setOnTouchListener true
        }


        binding.scaleView.setOnClickListener {
            if (isFullScreen) {
                binding.commentInputWrapper.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.commentInput.maxLines = 6
                binding.commentInput.minLines = 2
            } else {
               binding.commentInputWrapper.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.commentInput.maxLines = Int.MAX_VALUE
                binding.commentInput.minLines = 3
            }
            isFullScreen = !isFullScreen
        }

        binding.emoteToggle.setOnClickListener{
            isEmoteArea = !isEmoteArea
            if(isEmoteArea){
                binding.emoteLayout.layoutParams.height = emoteAreaHeight
            }else{
                binding.emoteLayout.layoutParams.height = 0
            }
            binding.emoteLayout.requestLayout()
            windowInsetsController.hide(WindowInsetsCompat.Type.ime())
        }

        binding.commentInput.setOnClickListener { v,  ->
            if(isEmoteArea) {
                isEmoteArea = false
                binding.emoteLayout.layoutParams.height = 0
                binding.emoteLayout.requestLayout()
            }
            windowInsetsController.show(WindowInsetsCompat.Type.ime())
        }
    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }
}