package com.xtguiyi.play.api.request

import com.xtguiyi.play.api.ApiService
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.model.BannerModel
import retrofit2.http.GET

interface CommonApi {
    @GET("mock/getBanners")
    suspend fun getBanners(): ResponseResult<List<BannerModel>>

    companion object {
        val instance: CommonApi by lazy {
            ApiService.retrofit
                .create(CommonApi::class.java)
        }
    }
}
