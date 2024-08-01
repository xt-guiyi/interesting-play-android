package com.xtguiyi.loveLife.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import com.xtguiyi.loveLife.entity.VideoInfo
import com.xtguiyi.loveLife.ui.videoPlayer.repository.VideoBriefIntroductionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

data class VideoBriefIntroductionUiState(
    var page: Int = 1,
    var pageSize: Int = 20,
    var total: Int = 0,
    var netWorkError: Boolean = false,
)

class VideoBriefIntroductionViewModel : ViewModel() {

    private val repository: VideoBriefIntroductionRepository = VideoBriefIntroductionRepository()
    // ui状态
    private val _uiState = MutableStateFlow(VideoBriefIntroductionUiState())
    val uiStateFlow: StateFlow<VideoBriefIntroductionUiState> = _uiState.asStateFlow()
    // 视频详情
    private var _videoInfo =  MutableStateFlow<VideoInfo?>(null)
    var videoInfoFlow: StateFlow<VideoInfo?> = _videoInfo.asStateFlow()
    // 视频列表
    private var _relatedVideoInfoList =  MutableStateFlow<List<VideoInfo>>(listOf())
    var relatedVideoInfoListFlow: StateFlow<List<VideoInfo>> = _relatedVideoInfoList.asStateFlow()

     suspend fun getVideoList() {
        val result = runCatching {
            repository.fetchVideoList(
                _uiState.value.page,
                _uiState.value.pageSize
            )
        }
        result.onFailure { e ->
            e.printStackTrace()
            if (e is IOException) {
                _uiState.update { currentState ->
                    currentState.copy(netWorkError = true)
                }
            }
        }
        result.onSuccess {
            if (it.isOk()) {
                _uiState.update { currentState ->
                    currentState.copy(
                        page = it.data.page,
                        pageSize = it.data.pageSize,
                        total = it.data.total,
                        netWorkError = false,
                    )
                }
                _relatedVideoInfoList.value = it.data.data
            }
        }
    }

    // 上拉加载
    suspend fun loadMore() {
        _uiState.update { currentState ->
            currentState.copy(page = currentState.page + 1)
        }
        getVideoList()
    }


    // 获取视频详情
    suspend fun getVideoDetail(id: Int) {
        val result = runCatching {
            repository.fetchVideoDetail(id)
        }
        result.onFailure { e ->
            e.printStackTrace()
            _uiState.update { currentState ->
                currentState.copy(
                    netWorkError = true,
                )
            }
        }
        result.onSuccess {
            if (it.isOk()) {
                _uiState.update { currentState ->
                    currentState.copy(
                        netWorkError = false,
                    )
                }
                _videoInfo.value = it.data
            }
        }
    }
}