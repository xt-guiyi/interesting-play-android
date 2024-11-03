package com.xtguiyi.play.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import com.xtguiyi.play.model.CommentInfoModel
import com.xtguiyi.play.ui.videoPlayer.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class CommentViewModel : ViewModel() {
    private val repository: CommentRepository = CommentRepository()
    // 网络状态
    private val _netWorkError = MutableStateFlow(false)
    // 相关视频列表
    private var _page: Int = 1;
    private var _pageSize: Int = 20;
    private var _total: Int = 0;
    private var _commentInfoList =  MutableStateFlow<List<CommentInfoModel>>(listOf())

    val total: Int  get() = _total
    var commentInfoListFlow: StateFlow<List<CommentInfoModel>> = _commentInfoList.asStateFlow()
    val netWorkErrorFlow: StateFlow<Boolean> = _netWorkError.asStateFlow()

     suspend fun getCommentList() {
        val result = runCatching {
            repository.fetchCommentList(_page,_pageSize)
        }
        result.onFailure { e ->
            if (e is IOException)  _netWorkError.value = true
            e.printStackTrace()
        }
        result.onSuccess {
            if (it.isOk() && it.data != null) {
                _page = it.data.page
                _pageSize = it.data.pageSize
                _total = it.data.total
                _commentInfoList.value = it.data.data
            }
            _netWorkError.value = false
        }
    }

    // 上拉加载
    suspend fun loadMore() {
        _page += 1
        getCommentList()
    }
}