package com.example.loveLife.ui.videoPlayer.repository

import com.example.loveLife.api.PageData
import com.example.loveLife.api.ResponseResult
import com.example.loveLife.api.request.VideosApi
import com.example.loveLife.entity.VideoInfo

class VideoBriefIntroductionRepository {
    private val videosApi = VideosApi.instance

    suspend fun fetchVideoDetail(id: Int): ResponseResult<VideoInfo>  {
         return videosApi.getVideoDetail(id)
    }

    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<VideoInfo>>>  {
        return videosApi.getVideoList(page,pageSize)
    }
}