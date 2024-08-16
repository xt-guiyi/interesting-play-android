package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.databinding.CommentDialogBinding

class CommentDialogFragment() :BottomSheetDialogFragment() {
    private lateinit var binding: CommentDialogBinding
    private lateinit var windowInsetsController: WindowInsetsControllerCompat


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 聚焦，显示软键盘
        dialog?.window?.let {
            windowInsetsController = WindowCompat.getInsetsController(it, it.decorView)
            binding.commentInput.requestFocus()
            // 解决低版本dialogFragment弹出软键盘无效，加300ms延迟
            binding.commentInput.postDelayed({
                windowInsetsController.show(WindowInsetsCompat.Type.ime())
            }, 300)
        }
        initView()
    }

    fun initView() {

    }

    companion object {
        const val TAG = "CommentDialogFragment"
    }
}