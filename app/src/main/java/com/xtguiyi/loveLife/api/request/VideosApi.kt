package com.xtguiyi.loveLife.api.request

import com.xtguiyi.loveLife.api.ApiService
import com.xtguiyi.loveLife.api.PageData
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.entity.VideoInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface VideosApi {
    @GET("mock/video/getVideoList")
    suspend fun getVideoList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<VideoInfo>>>


    @GET("mock/video/getVideoDetail")
    suspend fun getVideoDetail(@Query("id") id: Int): ResponseResult<VideoInfo>


    companion object {
        val instance: VideosApi by lazy {
            ApiService.retrofit
                .create(VideosApi::class.java)
        }
    }
}
