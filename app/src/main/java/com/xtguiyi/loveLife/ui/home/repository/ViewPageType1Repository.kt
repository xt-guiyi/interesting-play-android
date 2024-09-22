package com.xtguiyi.loveLife.ui.home.repository

import com.xtguiyi.loveLife.api.PageData
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.api.request.CommonApi
import com.xtguiyi.loveLife.api.request.VideosApi
import com.xtguiyi.loveLife.model.Banner
import com.xtguiyi.loveLife.model.VideoInfo

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