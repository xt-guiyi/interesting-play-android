package com.xtguiyi.loveLife.api.request

import com.xtguiyi.loveLife.api.ApiService
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.model.User
import retrofit2.http.GET

interface UserApi {
    @GET("hotkey/getUserInfo")
    suspend fun getUserInfo(): ResponseResult<MutableList<User>>

    companion object {
        val instance: UserApi by lazy {
            ApiService.retrofit
                .create(UserApi::class.java)
        }
    }
}
