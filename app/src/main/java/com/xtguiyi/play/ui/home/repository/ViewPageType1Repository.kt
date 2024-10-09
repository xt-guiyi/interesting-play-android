package com.xtguiyi.play.ui.home.repository

import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.api.request.CommonApi
import com.xtguiyi.play.api.request.VideosApi
import com.xtguiyi.play.model.Banner
import com.xtguiyi.play.model.VideoInfo

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