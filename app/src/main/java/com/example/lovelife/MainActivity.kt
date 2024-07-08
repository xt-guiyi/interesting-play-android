package com.example.lovelife

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.loveLife.R
import com.example.loveLife.databinding.ActivityMainBinding
import com.example.lovelife.base.BaseActivity
import com.hjq.toast.Toaster

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        init()
        setContentView(binding.root)
    }

    private fun init() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    Toaster.show("home")
                }
                R.id.menu_discover -> {
                    Toaster.show("discover")
                }
                R.id.menu_me -> {
                    Toaster.show("me")
                }
                else -> {}
            }
            return@setOnItemSelectedListener true
        }
    }
}