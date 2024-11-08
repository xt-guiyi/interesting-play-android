package com.xtguiyi.play.ui.discover.repository

import com.xtguiyi.play.api.PageData
import com.xtguiyi.play.api.ResponseResult
import com.xtguiyi.play.api.request.DiscoverApi
import com.xtguiyi.play.model.DiscoverInfoModel

class ViewPageType1Repository {
    private val discoverApi = DiscoverApi.instance

    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<DiscoverInfoModel>>>  {
         return discoverApi.getDiscoverList(page,pageSize)
    }
}