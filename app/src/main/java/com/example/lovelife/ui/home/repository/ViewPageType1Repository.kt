package com.example.lovelife.ui.home.repository

import com.example.lovelife.api.PageData
import com.example.lovelife.api.ResponseResult
import com.example.lovelife.api.request.CommonApi
import com.example.lovelife.api.request.VideosApi
import com.example.lovelife.entity.Banner
import com.example.lovelife.entity.VideoInfo

class ViewPageType1Repository {
    private val videosApi = VideosApi.instance
    private val commonApi = CommonApi.instance

    suspend fun fetchBanner(): ResponseResult<List<Banner>>  {
         return commonApi.getBanners()
    }

    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<VideoInfo>>>  {
         return videosApi.getVideoList(page,pageSize)
    }
}