package com.xtguiyi.play.api

import kotlinx.serialization.Serializable

/**
 * 分页数据
 * @param page 页码
 * @param pageSize 分页数
 * @param total 总数据
 * @param data 数据本体
 */
@Serializable
data class PageData<T>(val page: Int, val pageSize: Int, val total: Int, val data: T)
