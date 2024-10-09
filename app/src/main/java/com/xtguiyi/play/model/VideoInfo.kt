package com.xtguiyi.play.model

import kotlinx.serialization.Serializable

/**
 * 视频信息
 * @param id 视频id
 * @param title 视频标题
 * @param duration 视频时长
 * @param pic 视频封面地址
 * @param url 视频地址
 * @param views 视频播放量
 * @param coin 硬币量
 * @param danmaku 弹幕数
 * @param desc 视频描述
 * @param like 点赞数
 * @param dislike 踩数
 * @param reply 回复数
 * @param share 分享数
 * @param favorite 收藏数
 * @param owner 作者信息
 * @param pubDate 作者信息
 * */
@Serializable
data class VideoInfo(
    val id: Int,
    val title: String,
    val duration: Long,
    val pic: String,
    val url: String,
    val views: Long,
    val coin: Long,
    val danmaku: Int,
    val desc: String,
    val like: Long,
    val dislike: Long,
    val reply: Long,
    val share: Long,
    val favorite: Long,
    val owner: Owner,
    val pubDate: Long,
)
