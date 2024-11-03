package com.xtguiyi.play.api

import com.xtguiyi.play.BuildConfig


/**
 * api配置
 * */
object ApiConfiguration {
    public  val BASE_URL = if(BuildConfig.ENV == "DEV") "http://192.168.31.224:3000/" else "https://interesting-play-service-nest.vercel.app/"
//    public  val BASE_URL = if(BuildConfig.ENV == "DEV") "https://interesting-play-service-nest.vercel.app/" else "https://interesting-play-service-nest.vercel.app/"
}