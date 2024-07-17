package com.example.loveLife.api.request

import com.example.loveLife.api.ApiService
import com.example.loveLife.api.ResponseResult
import com.example.loveLife.entity.User
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
