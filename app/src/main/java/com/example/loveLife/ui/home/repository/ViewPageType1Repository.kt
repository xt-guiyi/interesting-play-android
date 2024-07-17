package com.example.loveLife.ui.home.repository

import com.example.loveLife.api.PageData
import com.example.loveLife.api.ResponseResult
import com.example.loveLife.api.request.CommonApi
import com.example.loveLife.api.request.VideosApi
import com.example.loveLife.entity.Banner
import com.example.loveLife.entity.VideoInfo

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