package com.example.loveLife.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    /**
     *初始化布局
     * */
    abstract fun initView()

    /**
     *初始化数据
     * */
    abstract fun initData()

    /**
     * 绑定侦听器
     * */
    abstract fun bindingListener()
}