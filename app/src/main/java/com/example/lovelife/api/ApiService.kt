package com.example.lovelife.api

import com.example.lovelife.api.ApiConfiguration.BASE_URL
import com.hjq.toast.Toaster
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit实例
 * */
object ApiService {

    // 自定义拦截器，用于添加 Token 到请求头
    private val authInterceptor = Interceptor { chain ->
        val token = "your_access_token_here"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        chain.proceed(request)
    }

    // 自定义拦截器，用于处理响应状态码
    private val responseInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            // 响应
            // 检查响应状态码
            if (!response.isSuccessful) {
                val errorStatus = ErrorStatus.entries.find { it.code == response.code() }
                val errorMessage = errorStatus?.errMsg ?: "未知错误"
                Toaster.show(errorMessage)
            }
            return response
        }
    }

    // OkHttpClient，添加拦截器
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .writeTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor) // 添加请求拦截器
        .addInterceptor(responseInterceptor) // 添加响应拦截器
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}