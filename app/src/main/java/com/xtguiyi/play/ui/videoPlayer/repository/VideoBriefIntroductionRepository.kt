package com.xtguiyi.play.ui.videoPlayer.repository

import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.api.request.VideosApi
import com.xtguiyi.play.model.VideoInfoModel

class VideoBriefIntroductionRepository {
    private val videosApi = VideosApi.instance

    // 获取视频详情
    suspend fun fetchVideoDetail(id: Int): ResponseResult<VideoInfoModel>  {
         return videosApi.getVideoDetail(id)
    }

    // 获取视频列表
    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<VideoInfoModel>>>  {
        return videosApi.getVideoList(page,pageSize)
    }
}