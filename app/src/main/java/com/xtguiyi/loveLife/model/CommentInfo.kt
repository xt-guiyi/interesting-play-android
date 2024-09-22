package com.xtguiyi.loveLife.model

/**
 * 评论信息
 * @param id 视频id
 * */
data class CommentInfo(
    val id: Int,
    val username: String,
    val pubDate: Long,
    val ipAddress: String,
    val content: String,
    val avatar: String,
    val like: Long,
)