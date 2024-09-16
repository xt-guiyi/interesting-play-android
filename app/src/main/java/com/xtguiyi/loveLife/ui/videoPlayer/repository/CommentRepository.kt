package com.xtguiyi.loveLife.ui.videoPlayer.repository

import com.xtguiyi.loveLife.api.PageData
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.api.request.VideosApi
import com.xtguiyi.loveLife.entity.CommentInfo

class CommentRepository {
    private val videosApi = VideosApi.instance


    // 获取视频列表
    suspend fun fetchCommentList(page: Int, pageSize: Int): ResponseResult<PageData<List<CommentInfo>>>  {
        return videosApi.getCommentList(page,pageSize)
    }
}