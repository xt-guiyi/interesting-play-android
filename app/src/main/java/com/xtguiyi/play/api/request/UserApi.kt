package com.xtguiyi.play.api.request

import com.xtguiyi.play.api.ApiService
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.model.User
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
