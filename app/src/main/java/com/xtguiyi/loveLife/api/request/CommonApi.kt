package com.xtguiyi.loveLife.api.request

import com.xtguiyi.loveLife.api.ApiService
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.entity.Banner
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
