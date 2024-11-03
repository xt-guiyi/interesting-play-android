package com.xtguiyi.play.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import com.xtguiyi.play.model.VideoInfoModel
import com.xtguiyi.play.ui.videoPlayer.repository.VideoBriefIntroductionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class BriefIntroductionViewModel : ViewModel() {
    private val repository: VideoBriefIntroductionRepository = VideoBriefIntroductionRepository()
    // 网络状态
    private val _netWorkError = MutableStateFlow(false)
    // 视频详情
    private var _videoInfo =  MutableStateFlow<VideoInfoModel?>(null)
    // 相关视频列表
    private var _page: Int = 1;
    private var _pageSize: Int = 20;
    private var _total: Int = 0;
    private var _relatedVideoInfoList =  MutableStateFlow<List<VideoInfoModel>>(listOf())

    val total: Int  get() = _total
    var relatedVideoInfoListFlow: StateFlow<List<VideoInfoModel>> = _relatedVideoInfoList.asStateFlow()
    var videoInfoFlow: StateFlow<VideoInfoModel?> = _videoInfo.asStateFlow()
    val netWorkErrorFlow: StateFlow<Boolean> = _netWorkError.asStateFlow()

     suspend fun getRelatedVideoList() {
        val result = runCatching {
            repository.fetchVideoList(_page,_pageSize)
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
                _relatedVideoInfoList.value = it.data.data
            }
            _netWorkError.value = false
        }
    }

    // 上拉加载
    suspend fun loadMore() {
        _page += 1
        getRelatedVideoList()
    }


    // 获取视频详情
    suspend fun getVideoDetail(id: Int) {
        val result = runCatching {
            repository.fetchVideoDetail(id)
        }
        result.onFailure { e ->
            if (e is IOException)  _netWorkError.value = true
            e.printStackTrace()
        }
        result.onSuccess {
            if (it.isOk()) {
                _videoInfo.value = it.data
            }
            _netWorkError.value = false
        }
    }
}