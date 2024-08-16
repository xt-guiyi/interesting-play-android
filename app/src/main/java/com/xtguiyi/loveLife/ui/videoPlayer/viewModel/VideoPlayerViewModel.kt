package com.xtguiyi.loveLife.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoPlayerViewModel : ViewModel() {
    // 导航栏高度
    private val _navHeight = MutableStateFlow(0)
    val navHeight: StateFlow<Int> = _navHeight.asStateFlow()

    fun setNavHeight(height: Int) {
//        Toaster.show("11-$height")
        _navHeight.value = height
    }
}