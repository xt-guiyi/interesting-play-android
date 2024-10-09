package com.xtguiyi.play.api

import com.xtguiyi.play.BuildConfig


/**
 * api配置
 * */
object ApiConfiguration {
    public  val BASE_URL = if(BuildConfig.ENV == "DEV") "http://192.168.2.78:3000/" else "https://android-server-nest.vercel.app/"
}