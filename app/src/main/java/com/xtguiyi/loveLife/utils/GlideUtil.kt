package com.xtguiyi.loveLife.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.xtguiyi.loveLife.R
import java.io.File

object GlideUtil {
    /**
     * 加载图片，开启缓存
     * @param url 图片地址
     * @param context 上下文
     * @param v 容器
     */
    fun loadUrl(url: String?, context: Context, v: ImageView) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.default_img) // 占位符，异常时显示的图片
            .error(R.drawable.default_img) // 错误时显示的图片
            .skipMemoryCache(false) //启用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //磁盘缓存策略
            .into(v)
    }

    /**
     * 加载图片，开启缓存
     * @param file 图片地址
     * @param context 上下文
     * @param v 容器
     */
    fun loadFile(file: File?, context: Context, v: ImageView) {
        //请求配置
        val options = RequestOptions.circleCropTransform()
        Glide.with(context).load(file)
            .placeholder(R.drawable.default_img) // 占位符，异常时显示的图片
            .error(R.drawable.default_img) // 错误时显示的图片
            .skipMemoryCache(false) //启用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //磁盘缓存策略
            .apply(options) // 圆形
            .into(v)
    }

    /**
     * 设置图片，不开启缓存
     * @param url
     * @param context 上下文
     * @param v 容器
     */
    fun loadUrlNoCache(url: String?, context: Context, v: ImageView) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .priority(Priority.HIGH)
            .skipMemoryCache(true) //不启动缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) //不启用磁盘策略
            .into(v)
    }

    /**
     * 加载圆形图片
     * @param url
     * @param context 上下文
     * @param v 容器
     */
    fun setUrlCircle(url: String?, context: Context, v: ImageView) {
        //请求配置
        val options = RequestOptions.circleCropTransform()
        Glide.with(context).load(url)
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .skipMemoryCache(false) //启用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .apply(options)// 圆形
            .into(v)
    }

    /**
     * 加载边框圆形图片
     * @param url
     * @param context 上下文
     * @param v 容器
     * @param borderWidth 边框宽度
     * @param borderColor 边框颜色
     */
    fun setUrlCircleBorder(url: String?, context: Context, v: ImageView, borderWidth: Float, borderColor: Int) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .skipMemoryCache(false) //启用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CircleBorderTransform(borderWidth, borderColor)) // 圆形
            .into(v)
    }

    /**
     * 加载圆角图片
     * @param url
     * @param context 上下文
     * @param v 容器
     * @param radius dp
     *
     * 注意：glide的图片裁剪和ImageView  scaleType有冲突，
     * bitmap会先圆角裁剪，再加载到ImageView中，如果bitmap图片尺寸大于ImageView尺寸，则会看不到
     * 使用CenterCrop()重载，会先将bitmap居中裁剪，再进行圆角处理，这样就能看到了。
     */
    fun setUrlRound(url: String?, context: Context, v: ImageView, radius: Int = 10) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .skipMemoryCache(false) // 启用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop(), RoundedCorners(radius))
            .into(v)
    }


    /**
     * 加载Gif图片
     * @param url
     * @param context 上下文
     * @param v 容器
     */
    fun setUrlGif(url: String?, context: Context, v: ImageView) {
        Glide.with(context).asGif().load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .into(v)
    }

    /**
     * 圆形Transform
     */
    internal class CircleBorderTransform(private val borderWidth: Float, private val borderColor: Int) : CircleCrop() {
        private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        init {
            borderPaint.color = borderColor
            borderPaint.style = Paint.Style.STROKE
            borderPaint.strokeWidth = borderWidth
        }

        override fun transform(
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap {
            //因为继承自CircleCrop，并且CircleCrop是圆形，在这里获取的bitmap是裁剪后的圆形bitmap
            val transform = super.transform(pool, toTransform, outWidth, outHeight)
            val canvas = Canvas(transform)
            val halfWidth = (outWidth / 2).toFloat()
            val halfHeight = (outHeight / 2).toFloat()
            canvas.drawCircle(
                halfWidth,
                halfHeight,
                halfWidth.coerceAtMost(halfHeight) - borderWidth / 2,
                borderPaint
            )
            canvas.setBitmap(null)
            return transform
        }
    }


}