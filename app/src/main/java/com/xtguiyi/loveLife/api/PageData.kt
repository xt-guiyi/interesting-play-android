package com.xtguiyi.loveLife.api

data class PageData<T>(val page: Int, val pageSize: Int, val total: Int, val data: T)
