package com.xtguiyi.play.api.request

import com.xtguiyi.play.api.ApiService
import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.model.CommentInfoModel
import com.xtguiyi.play.model.VideoInfoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface VideosApi {
    @GET("mock/getVideoList")
    suspend fun getVideoList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<VideoInfoModel>>>


    @GET("mock/getVideoDetail")
    suspend fun getVideoDetail(@Query("id") id: Int): ResponseResult<VideoInfoModel>

    @GET("mock/getCommentList")
    suspend fun getCommentList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<CommentInfoModel>>>


    companion object {
        val instance: VideosApi by lazy {
            ApiService.retrofit
                .create(VideosApi::class.java)
        }
    }
}
