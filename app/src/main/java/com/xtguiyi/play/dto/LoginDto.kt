package com.xtguiyi.play.dto

import kotlinx.serialization.Serializable

/**
 * 登录信息
 * @param username 用户名/手机号
 * @param password 密码
 * */
@Serializable
data class LoginDto(
    val username: String,
    val password: String,
)
