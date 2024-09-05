package com.xtguiyi.loveLife.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/***
 * 评论dialog数据
 * */
class CommentDialogViewModel : ViewModel() {
    // emoji unicode表情
    private val _emojiStr = MutableSharedFlow<String>(replay = 1)
    val emojiStr: SharedFlow<String> = _emojiStr

    suspend fun setEmojiStr(str: String) {
        // emit 可以发送相同的值
        _emojiStr.emit(str)
    }
}