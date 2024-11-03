package com.xtguiyi.play.model

import kotlinx.serialization.Serializable

@Serializable
data class BannerModel(
    val id: Int,
    val jumpUrl: String,
    val url: String
)