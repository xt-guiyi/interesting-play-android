package com.example.lovelife.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity : AppCompatActivity() {
    /**
     *初始化
     * */
    abstract fun init()
    /**
     * 绑定侦听器
     * */
    abstract fun bindingListener()
}