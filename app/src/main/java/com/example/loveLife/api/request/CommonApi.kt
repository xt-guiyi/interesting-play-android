package com.example.loveLife.api.request

import com.example.loveLife.api.ApiService
import com.example.loveLife.api.ResponseResult
import com.example.loveLife.entity.Banner
import retrofit2.http.GET

interface CommonApi {
    @GET("mock/common/getBanners")
    suspend fun getBanners(): ResponseResult<List<Banner>>

    companion object {
        val instance: CommonApi by lazy {
            ApiService.retrofit
                .create(CommonApi::class.java)
        }
    }
}
