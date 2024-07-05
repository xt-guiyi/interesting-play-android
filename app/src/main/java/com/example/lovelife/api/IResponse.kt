package com.example.lovelife.api

/**
 * 响应类
 */
data class IResponse<T>(val code: Int, val msg: String, val data: T) {
    /**
     * 判定接口返回是否正常
     */
    fun isFailed(): Boolean {
        return code != 0
    }
}