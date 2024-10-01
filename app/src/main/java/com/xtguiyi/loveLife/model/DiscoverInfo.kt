package com.xtguiyi.loveLife.model

import kotlinx.serialization.Serializable

/**
 * 发现信息
 * @param pic 封面地址
 * @param picW 封面地址宽度
 * @param picH 视频封面高度
 * @param reply 回复数
 * @param owner 作者信息
 * */
@Serializable
data class DiscoverInfo(
    val id: Int,
    val title: String,
    val pic: String,
    val picW: Int?,
    val picH: Int?,
    val reply: Long,
    val owner: Owner,
)
