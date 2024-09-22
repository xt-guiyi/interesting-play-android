package com.xtguiyi.loveLife.model

/**
 * 发现信息
 * @param pic 视频封面地址
 * @param reply 回复数
 * @param owner 作者信息
 * */
data class DiscoverInfo(
    val id: Int,
    val title: String,
    val pic: String,
    val reply: Long,
    val owner: Owner,
)
