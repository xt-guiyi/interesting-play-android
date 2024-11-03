package com.xtguiyi.play.api.request

import com.xtguiyi.play.api.ApiService
import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.model.DiscoverInfoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {
    @GET("mock/getDiscoverList")
    suspend fun getDiscoverList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<DiscoverInfoModel>>>

    companion object {
        val instance: DiscoverApi by lazy {
            ApiService.retrofit
                .create(DiscoverApi::class.java)
        }
    }
}
