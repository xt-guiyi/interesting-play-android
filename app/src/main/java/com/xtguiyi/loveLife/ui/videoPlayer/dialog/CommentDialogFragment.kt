package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.DialogCommentBinding
import com.xtguiyi.loveLife.ui.videoPlayer.adapter.EmojiViewPageAdapter
import com.xtguiyi.loveLife.ui.videoPlayer.viewModel.CommentDialogViewModel
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlinx.coroutines.launch

class CommentDialogFragment(isOpenEmoji : Boolean = false) : DialogFragment() {
    private lateinit var binding: DialogCommentBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private val commentDialogViewModel: CommentDialogViewModel by viewModels()
    private var isPrepare = false
    private var isFullScreen = false
    private var isBottomLayout = isOpenEmoji
    private var bottomLayoutHeight = (DisplayUtil.getScreenHeight() * 0.4).toInt()

    init {
        setStyle(STYLE_NORMAL, R.style.CustomDialog1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommentBinding.inflate(inflater, container, false)
        configuration()
        initView()
        initData()
        bindingListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 聚焦，显示软键盘， 配置上下文
        binding.commentInput.customInsertionActionModeCallback =  object : ActionMode.Callback2() {
            override fun onCreateActionMode(
                mode: ActionMode?,
                menu: Menu?
            ): Boolean {
                return false
            }

            override fun onPrepareActionMode(
                mode: ActionMode?,
                menu: Menu?
            ): Boolean {
                return true
            }

            override fun onActionItemClicked(
                mode: ActionMode?,
                item: MenuItem?
            ): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) { }

        }
        binding.commentInput.customSelectionActionModeCallback = object : ActionMode.Callback2() {
            override fun onCreateActionMode(
                mode: ActionMode?,
                menu: Menu?
            ): Boolean {
                return false
            }

            override fun onPrepareActionMode(
                mode: ActionMode?,
                menu: Menu?
            ): Boolean {
                return false
            }

            override fun onActionItemClicked(
                mode: ActionMode?,
                item: MenuItem?
            ): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) { }

        }
        if(!isBottomLayout) {
               binding.commentInput.requestFocus()
               windowInsetsController.show(WindowInsetsCompat.Type.ime())
        }
    }

    override fun onResume() {
        super.onResume()
        isPrepare = false
        if(!binding.commentInput.isFocused && !isBottomLayout)  binding.commentInput.requestFocus()
    }

    private fun configuration() {
        // 配置窗口变化
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            if(!isResumed) return@setOnApplyWindowInsetsListener insets // 切后台，不应修改状态
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBar = insets.getInsets(WindowInsetsCompat.Type.ime())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible) bottomLayoutHeight = imeBar.bottom - systemBars.bottom
            val topPadding = systemBars.top
            val bottomPadding =  if (imeVisible) imeBar.bottom else systemBars.bottom
            v.setPadding(0, topPadding, 0, bottomPadding)
            binding.bottomActionLayout.layoutParams.height = if (isBottomLayout) bottomLayoutHeight else 0
            binding.bottomActionLayout.requestLayout()
            binding.emojiViewpage2.visibility = if (isBottomLayout) ViewGroup.VISIBLE else ViewGroup.GONE
            if (!imeVisible && !isBottomLayout)  {
                if (!isPrepare) {
                    binding.root.postDelayed({isPrepare = true}, 500)
                }else {
                    dismiss()
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

    fun initView() {
        initEmoji()
    }

    private fun initEmoji() {
        val adapter = EmojiViewPageAdapter(this, 2)
        binding.emojiViewpage2.adapter = adapter
    }

    fun initData() {
        lifecycleScope.launch{
           launch{
               commentDialogViewModel.emojiStr.collect{
                   setEmojiStr(it)
               }
           }
            launch{
                commentDialogViewModel.emojiImage.collect{
                    setEmojiDrawable(it)
                }
            }
        }
    }

    private fun setEmojiStr(str: String) {
        val selectionStart = binding.commentInput.selectionStart
        if (selectionStart == -1 ) return
        val stringBuilder = SpannableStringBuilder(binding.commentInput.text)
        stringBuilder.insert(selectionStart,str)
        binding.commentInput.setText(stringBuilder)
        binding.commentInput.setSelection(selectionStart + str.length)
    }

    private fun setEmojiDrawable(str: String) {
        val selectionStart = binding.commentInput.selectionStart
        if (selectionStart == -1 ) return
        val round = DisplayUtil.dip2px(22f)
        Glide.with(requireContext())
            .load(str)
            .priority(Priority.HIGH)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE) //不启用磁盘策略
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    drawable: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    if (drawable is Animatable)  drawable.start()
                    drawable.setBounds(0,0,round,round)
                    val stringBuilder = SpannableStringBuilder(binding.commentInput.text)
                    stringBuilder.insert(selectionStart, "\r")
                    val imageSpan = ImageSpan(drawable,DynamicDrawableSpan.ALIGN_BOTTOM)
                    stringBuilder.setSpan(imageSpan, selectionStart, selectionStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.commentInput.setText(stringBuilder)
                    binding.commentInput.setSelection(selectionStart + 1)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

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
            } else {
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }
        }

        binding.commentInput.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(
                v: View?,
                event: MotionEvent?
            ): Boolean {
                isBottomLayout = false
                return false
            }

        })
    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }


}