package com.xtguiyi.loveLife.ui.videoPlayer.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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

    // emoji unicode表情
    private val _emojiBitmap = MutableSharedFlow<Bitmap>(replay = 1)
    val emojiBitmap: SharedFlow<Bitmap> = _emojiBitmap

    suspend fun setEmojiStr(str: String) {
        // emit 可以发送相同的值
        _emojiStr.emit(str)
    }

    suspend fun setEmojiBitmap(bitmap: Bitmap) {
        // emit 可以发送相同的值
        _emojiBitmap.emit(bitmap)
    }
}