package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.hjq.toast.Toaster
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogCommentBinding
import com.xtguiyi.loveLife.entity.User
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.VideoPlayViewPageAdapter
import kotlinx.serialization.json.Json

class CommentDialogFragment : DialogFragment() {
    private lateinit var binding: DialogCommentBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var isFirst = true
    private var isFullScreen = false
    private var isBottomLayout = false
    private var bottomLayoutHeight = 0

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

    override fun onStart() {
        super.onStart()
        // 聚焦，显示软键盘
        binding.commentInput.requestFocus()
        windowInsetsController.show(WindowInsetsCompat.Type.ime())
    }

    override fun onResume() {
        super.onResume()
        if(!binding.commentInput.isFocused) binding.commentInput.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        binding.commentInput.clearFocus()
    }

    private fun configuration() {
        // 配置窗口变化
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBar = insets.getInsets(WindowInsetsCompat.Type.ime())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible) bottomLayoutHeight = imeBar.bottom - systemBars.bottom
            val topPadding = systemBars.top
            val bottomPadding =  if (imeVisible) imeBar.bottom else systemBars.bottom
            v.setPadding(0, topPadding, 0, bottomPadding)
            binding.bottomActionLayout.layoutParams.height = if (isBottomLayout) bottomLayoutHeight else 0
            binding.bottomActionLayout.requestLayout()
            if (!imeVisible && !isBottomLayout)  {
                if (isFirst) {
                    binding.root.postDelayed({isFirst = false}, 300)
                }else {
                    // 延300ms关闭
                    binding.root.postDelayed({
                        dismiss()
                    },300)
                }
            }
             // CONSUMED   知识扩展：还可以返回这个表示已经被消费了,就不会处理inset
            insets
        }
        dialog?.window?.apply {
            windowInsetsController = WindowCompat.getInsetsController(this, decorView)
            windowInsetsController.isAppearanceLightNavigationBars = true
            windowInsetsController.isAppearanceLightStatusBars = false
            // 配置系统栏颜色
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = ResourcesCompat.getColor(resources, R.color.green_100, null)
            // 设置弹窗外的背景透明度
            setDimAmount(0.3f)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // decorView适配系统栏区域，可以衍生到系统栏
            // 这里必须明确设置为false, 才能让setOnApplyWindowInsetsListener处理binding.root节点
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
    }

    fun initView() {}

    private fun initEmoji() {
        val adapter = EmojiViewPageAdapter(requireActivity(), 2)
        binding.emojiViewpage2.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bindingListener() {
        // 关闭dialog
        binding.postCommentDialog.setOnTouchListener { _, event ->
            val childRect = Rect()
            binding.commentBox.getHitRect(childRect)
            if (!childRect.contains(event.x.toInt(), event.y.toInt())) {
                if (!isFullScreen) dismiss()
            }
            return@setOnTouchListener true
        }

        // 全屏
        binding.scaleView.setOnClickListener {
            if (isFullScreen) {
                binding.commentBox.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT
                binding.commentInput.maxLines = 6
                binding.commentInput.minLines = 2
            } else {
                binding.commentBox.layoutParams.height =
                    ViewGroup.LayoutParams.MATCH_PARENT
                binding.commentInput.maxLines = Int.MAX_VALUE
                binding.commentInput.minLines = 3
            }
            isFullScreen = !isFullScreen
        }

        // 显示表情区域
        binding.emoteToggle.setOnClickListener {
            isBottomLayout = !isBottomLayout
            if (isBottomLayout) {
                windowInsetsController.hide(WindowInsetsCompat.Type.ime())
                if(binding.emojiViewpage2.adapter == null) initEmoji()
            } else {
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }
        }


        binding.commentInput.setOnClickListener {
            if (isBottomLayout)  isBottomLayout = false
        }
    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }
}