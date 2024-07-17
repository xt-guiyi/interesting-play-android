package com.example.loveLife.entity

/**
 * 视频信息
 * @param id 视频id
 * @param title 视频标题
 * @param duration 视频时长
 * @param author 视频作者
 * @param url 视频封面地址
 * @param views 视频播放量
 * */
data class VideoInfo(val id: String, val title: String, val duration: Long, val author: String, val url: String, val views: Long )
