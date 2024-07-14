package com.example.lovelife.api.request

import com.example.lovelife.api.ApiService
import com.example.lovelife.api.PageData
import com.example.lovelife.api.ResponseResult
import com.example.lovelife.entity.VideoInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface VideosApi {
    @GET("mock/video/getVideoList")
    suspend fun getVideoList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<VideoInfo>>>

    companion object {
        val instance: VideosApi by lazy {
            ApiService.retrofit
                .create(VideosApi::class.java)
        }
    }
}
