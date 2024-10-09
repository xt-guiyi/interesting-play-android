package com.xtguiyi.play.ui.videoPlayer.repository

import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.api.request.VideosApi
import com.xtguiyi.play.model.CommentInfo

class CommentRepository {
    private val videosApi = VideosApi.instance


    // 获取视频列表
    suspend fun fetchCommentList(page: Int, pageSize: Int): ResponseResult<PageData<List<CommentInfo>>>  {
        return videosApi.getCommentList(page,pageSize)
    }
}