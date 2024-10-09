package com.xtguiyi.play.ui.videoPlayer.viewModel

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

    // emoji 图片表情
    private val _emojiImage = MutableSharedFlow<String>(replay = 1)
    val emojiImage: SharedFlow<String> = _emojiImage

    suspend fun setEmojiStr(str: String) {
        // emit 可以发送相同的值
        _emojiStr.emit(str)
    }

    suspend fun setEmojiImage(bitmap: String) {
        // emit 可以发送相同的值
        _emojiImage.emit(bitmap)
    }
}