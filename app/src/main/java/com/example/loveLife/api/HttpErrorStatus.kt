package com.example.loveLife.api

/**
 * HTTP错误状态码枚举类
 */
enum class HttpErrorStatus(val code: Int, val errMsg: String) {
    /**
     * 一个错误请求
     */
    BAD_REQUEST(400, "错误请求"),

    /**
     * 当前请求需要用户验证
     */
    UNAUTHORIZED(401, "请求未授权"),

    /**
     * 资源不可用。服务器理解客户的请求，但拒绝处理它
     */
    FORBIDDEN(403, "服务器拒绝请求"),

    /**
     * 无法找到指定位置的资源
     */
    NOT_FOUND(404, "无法找到指定位置的资源"),

    /**
     * 禁用请求中指定的方法
     */
    METHOD_NOT_ALLOWED(405, "方法禁用"),

    /**
     * 在服务器许可的等待时间内，客户一直没有发出任何请求
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 服务器遇到了意料不到的情况，不能完成客户的请求
     */
    INTERNAL_SERVER_ERROR(500, "服务器错误"),

    /**
     * 服务器作为网关或者代理时，为了完成请求访问下一个服务器，但该服务器返回了非法的应答
     */
    BAD_GATEWAY(502, "网关错误"),

    /**
     * 服务器由于维护或者负载过重未能应答
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 服务器作为网关或代理，但是没有及时从上游服务器收到请求
     */
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * HTTP 版本不受支持。服务器不支持请求中所用的 HTTP 协议版本。
     */
    HTTP_VERSION_NOT_SUPPORTED(505 , "HTTP 版本不受支持"),
}