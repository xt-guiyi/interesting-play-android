package com.example.lovelife.api.request

import com.example.lovelife.api.ApiService
import com.example.lovelife.api.ResponseResult
import com.example.lovelife.entity.Banner
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
