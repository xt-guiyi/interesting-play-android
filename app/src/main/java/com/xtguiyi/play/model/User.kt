package com.xtguiyi.play.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val username: String, val age: Int, val email: String)
