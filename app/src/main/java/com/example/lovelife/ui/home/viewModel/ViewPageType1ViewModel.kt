package com.example.lovelife.ui.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lovelife.entity.Banner
import com.example.lovelife.entity.VideoInfo
import com.example.lovelife.ui.home.repository.ViewPageType1Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


data class ViewPageType1UiState(
    var page: Int = 1,
    var pageSize: Int = 10,
    var total: Int = 0,
    var updateType: Int = 0,
    var netWorkError: Boolean = false,
    var isLoading: Boolean = false
)


class ViewPageType1ViewModel : ViewModel() {

    private val repository: ViewPageType1Repository = ViewPageType1Repository()
    private val _uiState = MutableStateFlow(ViewPageType1UiState())
    val uiStateFlow: StateFlow<ViewPageType1UiState> = _uiState.asStateFlow()

    // 视频列表数据
    private val _videoList: MutableStateFlow<List<VideoInfo>> = MutableStateFlow(emptyList())
    val videoListFlow: StateFlow<List<VideoInfo>> = _videoList.asStateFlow()

    // 轮播图数据
    var bannersFlow: Flow<List<Banner>> = flow {
        val result = runCatching { repository.fetchBanner() }
        result.onFailure { e ->
            e.printStackTrace()
            emit(emptyList())
        }
        result.onSuccess {
            if (it.isOk()) {
                emit(it.data)
            }
        }
    }

    init {
        _uiState.update { currentState ->
            currentState.copy(page = 1, updateType = 1, netWorkError = false, isLoading = true)
        }
        getVideoList()
    }

    private fun getVideoList() {
        viewModelScope.launch {
            val result = runCatching {
//                delay(300) // 延迟300ms,防止滑动页面期间渲染数据，导致卡顿，不开启离屏加载使用
                repository.fetchVideoList(
                    _uiState.value.page,
                    _uiState.value.pageSize
                )
            }
            _uiState.update { currentState ->
                currentState.copy(isLoading = false)
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
                        currentState.copy(page = it.data.page, pageSize = it.data.pageSize, total = it.data.total)
                    }
                    // 更新 videoList
                    _videoList.update { _ ->
                        it.data.data
                    }
                }
            }
        }
    }

    // 下拉刷新
    fun refresh() {
        _uiState.update { currentState ->
            currentState.copy(page = 1, updateType = 1, netWorkError = false)
        }
        getVideoList()
    }

    // 上拉加载
    fun loadMore() {
        _uiState.update { currentState ->
            currentState.copy(page = currentState.page + 1, updateType = 0, netWorkError = false)
        }
        getVideoList()
    }

    fun retry() {
        _uiState.update { currentState ->
            currentState.copy(page = 1, updateType = 1, netWorkError = false, isLoading = true)
        }
        // 重新触发bannersFlow流
        bannersFlow = flow {
            val result = runCatching { repository.fetchBanner() }
            result.onFailure { e ->
                e.printStackTrace()
                emit(emptyList())
            }
            result.onSuccess {
                if (it.isOk()) {
                    emit(it.data)
                }
            }
        }
        getVideoList()
    }
}