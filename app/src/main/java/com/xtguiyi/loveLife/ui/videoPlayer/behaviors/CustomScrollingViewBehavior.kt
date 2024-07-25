package com.xtguiyi.loveLife.ui.videoPlayer.behaviors

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.xtguiyi.loveLife.R
import com.xtguiyi.loveLife.utils.DisplayUtil
import kotlin.math.abs

class CustomScrollingViewBehavior(val context: Context, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        dependency: View
    ): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
    }

}