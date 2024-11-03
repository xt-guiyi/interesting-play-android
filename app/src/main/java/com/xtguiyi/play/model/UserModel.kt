package com.xtguiyi.play.model

import kotlinx.serialization.Serializable

/**
 * 用户模型
 * @param username 用户名
 * @param age 年龄
 * @param avatar 头像
 * @param introduction 简介
 */
@Serializable
data class UserModel(val username: String, val age: Int, val avatar: String, val introduction: String)
