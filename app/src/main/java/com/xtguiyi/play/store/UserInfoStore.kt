package com.xtguiyi.play.store

import com.xtguiyi.play.model.UserModel

/**
 * 全局可访问单例类-用户信息
 * */
  object UserInfoStore {
    private var userInfo: UserModel? = null

    suspend fun getUserInfo(): UserModel? {
        if(userInfo !=null) return userInfo
        DataStoreManager.getUserInfo.collect{
            userInfo = it
        }
        return userInfo
    }

    suspend fun setUserInfo(data: UserModel?) {
        userInfo = data
        if (data != null) {
           DataStoreManager.setUserInfo(data)
        }
    }

    suspend fun removeUserInfo() {
        userInfo = null
        DataStoreManager.clearUserInfo()
    }
}
