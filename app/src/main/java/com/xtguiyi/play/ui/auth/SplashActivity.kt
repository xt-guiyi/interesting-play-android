package com.xtguiyi.play.ui.auth

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.xtguiyi.play.databinding.ActivitySplashBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bytedance.danmaku.render.engine.data.DataManager
import com.hjq.toast.Toaster
import com.xtguiyi.play.MainActivity
import com.xtguiyi.play.R
import com.xtguiyi.play.base.BaseActivity
import com.xtguiyi.play.store.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        initData()
        bindingListener()
        setContentView(binding.root)
    }

    override fun initView() { }
    override fun initData() {
        // 根据用户信息或者token判断有没有登录
        lifecycleScope.launch{
            DataStoreManager.getUserInfo.collect{
                delay(0) // 设置启动屏显示时间
                var intent: Intent= if(it != null) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                }else {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 使用 ActivityOptions 设置无动画
                    startActivity(intent,ActivityOptions.makeCustomAnimation(this@SplashActivity, 0, 0, 0).toBundle())
                } else {
                    startActivity(intent)
                    // 旧版使用原来的方式
                    overridePendingTransition(0, 0)
                }
            }
        }
    }
    override fun bindingListener() {}
}