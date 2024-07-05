package com.example.lovelife

import android.app.Application
import android.util.Log
import com.hjq.toast.Toaster

class BaseApplication : Application() {
//    private var executorService: ExecutorService = Executors.newFixedThreadPool(4) // 全局线程池
//    private var exampleRepository: ExampleRepository= ExampleRepository(MokeAPi.create())
//    fun getExecutorService() = executorService
//    fun getExampleRepository() = exampleRepository
    init {
        Log.i("Toaster","应用创建")
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化 Toast 框架
        Toaster.init(this);
    }
}