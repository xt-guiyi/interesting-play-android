package com.xtguiyi.loveLife

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.hjq.toast.Toaster

class MainApplication : Application() {
//    private var executorService: ExecutorService = Executors.newFixedThreadPool(4) // 全局线程池

    init {
        Log.i("Toaster","应用创建")
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        // 初始化 Toast 框架
        Toaster.init(this);
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext(): Context {
            return context
        }
    }
}