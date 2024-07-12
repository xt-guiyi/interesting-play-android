package com.example.lovelife.api

/**
 * 响应类
 * @param code 返回值： 0：成功
 * @param message 错误信息
 * @param data 数据本体
 */
data class IResponse<T>(val code: Int, val message: String, val data: T) {
    /**
     * 判定接口返回是否正常
     */
    fun isFailed(): Boolean {
        return code != 0
    }
}