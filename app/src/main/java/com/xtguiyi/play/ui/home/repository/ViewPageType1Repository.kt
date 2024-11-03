package com.xtguiyi.play.ui.home.repository

import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.api.request.CommonApi
import com.xtguiyi.play.api.request.VideosApi
import com.xtguiyi.play.model.BannerModel
import com.xtguiyi.play.model.VideoInfoModel

class ViewPageType1Repository {
    private val videosApi = VideosApi.instance
    private val commonApi = CommonApi.instance

    suspend fun fetchBanner(): ResponseResult<List<BannerModel>>  {
         return commonApi.getBanners()
    }

    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<VideoInfoModel>>>  {
         return videosApi.getVideoList(page,pageSize)
    }
}