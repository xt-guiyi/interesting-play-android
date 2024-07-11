package com.example.lovelife.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    /**
     *初始化
     * */
    abstract fun init()
    /**
     * 绑定侦听器
     * */
    abstract fun bindingListener()
}