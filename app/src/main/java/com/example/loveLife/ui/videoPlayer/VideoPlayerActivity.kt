package com.example.loveLife.ui.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.loveLife.base.BaseActivity
import com.example.loveLife.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : BaseActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun bindingListener() {
        TODO("Not yet implemented")
    }
}