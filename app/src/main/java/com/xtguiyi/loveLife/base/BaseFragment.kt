package com.xtguiyi.loveLife.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
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