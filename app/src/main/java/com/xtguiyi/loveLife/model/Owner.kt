package com.xtguiyi.loveLife.model

import kotlinx.serialization.Serializable

/**
 * 视频信息
 * @param mid 作者id
 * @param face 作者头像
 * @param fans 粉丝数
 * @param videos 视频数
 * @param name 作者名字
 * */
@Serializable
data class Owner(
    val mid: Long,
    val face: String,
    val fans: Long,
    val videos: Long,
    val name: String
)