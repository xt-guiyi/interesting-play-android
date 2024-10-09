package com.xtguiyi.play.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoPlayerViewModel : ViewModel() {
    // 导航栏高度
    private val _navHeight = MutableStateFlow(0)
    val navHeight: StateFlow<Int> = _navHeight.asStateFlow()

    // 偏移距离
    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset.asStateFlow()

    fun setNavHeight(height: Int) {
        _navHeight.value = height
    }


    fun setOffset(height: Int) {
        _offset.value = height
    }
}