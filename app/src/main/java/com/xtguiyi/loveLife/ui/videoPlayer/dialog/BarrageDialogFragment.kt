package com.xtguiyi.loveLife.ui.videoPlayer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.xtguiyi.loveLife.R

class BarrageDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.barrage_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setStyle(DialogFragment.STYLE_NO_FRAME,0)
    }

    override fun getTheme(): Int {
        return R.style.MyFullScreenDialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            // 设置弹窗外的背景透明度

            setDimAmount(0.3f)
            // 设置弹窗的背景透明，来显示圆角，以及去掉padding
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }
}