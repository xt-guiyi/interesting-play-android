package com.xtguiyi.play.api.request

import com.xtguiyi.play.api.ApiService
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.dto.LoginDto
import com.xtguiyi.play.model.UserModel
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Headers

interface UserApi {

    /**
     * 获取用户信息
     * */
    @GET("mock/getUserInfo")
    suspend fun getUserInfo(): ResponseResult<UserModel>


    /**
     * 登录api
     * @param loginDto 登录信息
     * */
    @POST("mock/login")
    suspend fun login(@Body loginDto: LoginDto): ResponseResult<String>


    /**
     * 注册api
     * @param username 用户名/手机号
     * @param password 密码
     * */
    @POST("mock/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String, @Field("password") password: String): ResponseResult<String>

    companion object {
        val instance: UserApi by lazy {
            ApiService.retrofit
                .create(UserApi::class.java)
        }
    }
}
