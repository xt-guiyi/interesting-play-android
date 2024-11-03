package com.xtguiyi.play

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hjq.toast.Toaster

class MainApplication : Application() {

    init {
        Log.i("Toaster","应用创建")
    }

    override fun onCreate() {
        super.onCreate()
        GlobContext = applicationContext
        // 初始化 Toast 框架
        Toaster.init(this);
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var GlobContext: Context

        /**
         * 跳转activity
         * @param activityClass 目标activity
         * @param flags 跳转标志
         * */
        fun startActivity(activityClass: Class<out Activity>,  flags: Int?) {
            val intent = Intent(context, activityClass)
            flags?.let { intent.addFlags(it) }
            context.startActivity(intent)
        }

        /**
         * 全局上下文
         * */
        val context: Context
            get() = this.GlobContext
    }
}