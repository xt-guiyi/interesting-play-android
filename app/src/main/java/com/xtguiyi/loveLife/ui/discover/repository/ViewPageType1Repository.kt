package com.xtguiyi.loveLife.ui.discover.repository

import com.xtguiyi.loveLife.api.PageData
import com.xtguiyi.loveLife.api.ResponseResult
import com.xtguiyi.loveLife.api.request.DiscoverApi
import com.xtguiyi.loveLife.model.DiscoverInfo

class ViewPageType1Repository {
    private val discoverApi = DiscoverApi.instance

    suspend fun fetchVideoList(page: Int, pageSize: Int): ResponseResult<PageData<List<DiscoverInfo>>>  {
         return discoverApi.getDiscoverList(page,pageSize)
    }
}