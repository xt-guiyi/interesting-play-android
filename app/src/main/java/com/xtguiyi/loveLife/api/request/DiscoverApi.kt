package com.xtguiyi.loveLife.api.request

import com.xtguiyi.loveLife.api.ApiService
import com.xtguiyi.loveLife.api.PageData
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.model.DiscoverInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {
    @GET("mock/getDiscoverList")
    suspend fun getDiscoverList(@Query("page") page: Int, @Query("pageSize") size: Int): ResponseResult<PageData<List<DiscoverInfo>>>

    companion object {
        val instance: DiscoverApi by lazy {
            ApiService.retrofit
                .create(DiscoverApi::class.java)
        }
    }
}
