package com.xtguiyi.play.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.xtguiyi.play.databinding.ActivityLoginBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.Toaster
import android.content.Intent
import com.xtguiyi.play.MainActivity
import com.xtguiyi.play.api.request.UserApi
import com.xtguiyi.play.base.BaseActivity
import com.xtguiyi.play.dto.LoginDto
import com.xtguiyi.play.store.DataStoreManager
import com.xtguiyi.play.store.UserInfoStore
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val regex = Regex("^(1[3-9]\\d{9})\$")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
    override fun initData() {}

    override fun bindingListener() {
        binding.submit.setOnClickListener {
            val phone = binding.phone.text.toString()
            val password = binding.password.text.toString()
            // 校验手机号格式
           if( regex.matches(phone)) {
              lifecycleScope.launch {
                 val result = runCatching { UserApi.instance.login(LoginDto(phone,password)) }
                  result.onSuccess {
                      if(it.isOk() && it.data != null) {
                          Toaster.show("登录成功")
                          DataStoreManager.setToken(it.data)
                          getUserInfo()
                          val intent = Intent(this@LoginActivity, MainActivity::class.java)
                          startActivity(intent)
                          // 关闭并启动新的 Activity
                          finish()
                      }else {
                          Toaster.show(it.message)
                      }
                  }
              }
           }else {
               Toaster.show("手机号格式错误")
           }
        }
    }

    // 获取用户信息
    suspend fun getUserInfo () {
         val result = runCatching { UserApi.instance.getUserInfo() }
         result.onSuccess {
             if(it.isOk() && it.data != null) {
                 UserInfoStore.setUserInfo(it.data)
             }
         }
        result.onFailure {
            Toaster.show("获取用户信息失败")
        }
    }
}