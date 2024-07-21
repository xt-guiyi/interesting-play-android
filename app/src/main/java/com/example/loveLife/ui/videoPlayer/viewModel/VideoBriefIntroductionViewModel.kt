package com.example.loveLife.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import com.example.loveLife.entity.VideoInfo
import com.example.loveLife.ui.videoPlayer.repository.VideoBriefIntroductionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

data class VideoBriefIntroductionUiState(
    var page: Int = 1,
    var pageSize: Int = 10,
    var total: Int = 0,
    var updateType: Int = 0,
    var netWorkError: Boolean = false,
    var videoInfo: VideoInfo? = null,
    var relatedVideoInfoList: List<VideoInfo>? = null
)

class VideoBriefIntroductionViewModel : ViewModel() {

    private val repository: VideoBriefIntroductionRepository = VideoBriefIntroductionRepository()

    private val _uiState = MutableStateFlow(VideoBriefIntroductionUiState())
    val uiStateFlow: StateFlow<VideoBriefIntroductionUiState> = _uiState.asStateFlow()

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
                        relatedVideoInfoList = it.data.data
                    )
                }
            }
        }
    }

    // 上拉加载
    suspend fun loadMore() {
        _uiState.update { currentState ->
            currentState.copy(page = currentState.page + 1, updateType = 0)
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
                        videoInfo = it.data
                    )
                }
            }
        }
    }
}