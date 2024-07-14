package com.example.lovelife.api.request

import com.example.lovelife.api.ApiService
import com.example.lovelife.api.ResponseResult
import com.example.lovelife.entity.User
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
