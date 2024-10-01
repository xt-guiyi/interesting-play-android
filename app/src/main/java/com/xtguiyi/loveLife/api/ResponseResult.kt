package com.xtguiyi.loveLife.api

import kotlinx.serialization.Serializable

/**
 * 响应类
 * @param code 返回值： 200：成功
 * @param message 错误信息
 * @param data 数据本体
 */
@Serializable
data class ResponseResult<T>(val code: Int, val message: String, val data: T) {
    /**
     * 判定接口返回是否正常
     */
    fun isOk(): Boolean {
        return code == 200
    }
}