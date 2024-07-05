package com.example.lovelife.api

import com.example.lovelife.entity.User
import retrofit2.http.GET

interface UserApi {
    @GET("hotkey/getUserInfo")
    suspend fun getUserInfo(): IResponse<User>

    companion object {
        val instance: UserApi by lazy {
            ApiService.retrofit
                .create(UserApi::class.java)
        }
    }
}