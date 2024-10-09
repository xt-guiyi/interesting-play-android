package com.xtguiyi.play.api

import com.hjq.toast.Toaster
import com.xtguiyi.play.api.ApiConfiguration.BASE_URL
import com.xtguiyi.play.store.PersistentData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit实例
 * */
object ApiService {
    private var token: String? = ""
    init {
        CoroutineScope(Dispatchers.IO).launch {
            PersistentData.token.collect { value ->
                token = value
                println("Token updated: $token")
            }
        }
    }

    // 自定义拦截器，用于添加 Token 到请求头
    private val authInterceptor = Interceptor { chain ->
        val request  = if(token != null ) {
            chain.request().newBuilder()
                .addHeader("Authorization", token!!)
                .build()
        }else {
            chain.request().newBuilder()
                .build()
        }

        chain.proceed(request)
    }

    // 自定义拦截器，用于处理响应状态码
    private val responseInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                // http状态非200
                val httpErrorStatus = HttpErrorStatus.entries.find { it.code == response.code() }
                val errorMessage = httpErrorStatus?.errMsg ?: "未知错误"
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
            .addConverterFactory(
                Json.asConverterFactory(
                    MediaType.get("application/json; charset=UTF8")
                )
            )
            .client(okHttpClient)
            .build()
    }
}