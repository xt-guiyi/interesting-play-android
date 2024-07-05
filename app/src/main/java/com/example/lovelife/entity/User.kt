package com.example.lovelife.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(val username: String, val age: Int, val email: String)
