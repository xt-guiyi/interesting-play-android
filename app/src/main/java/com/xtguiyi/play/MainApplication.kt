package com.xtguiyi.play

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hjq.toast.Toaster
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "persistent_data")
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

        val context: Context
            get() = this.GlobContext
    }
}