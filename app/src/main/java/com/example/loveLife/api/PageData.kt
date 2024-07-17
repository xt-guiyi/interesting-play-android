package com.example.loveLife.api

data class PageData<T>(val page: Int, val pageSize: Int, val total: Int, val data: T)
