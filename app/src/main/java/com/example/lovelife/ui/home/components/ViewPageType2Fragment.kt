package com.example.lovelife.ui.home.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loveLife.R
import com.example.loveLife.databinding.FragmentViewPageType1Binding
import com.example.loveLife.databinding.FragmentViewPageType2Binding
import com.example.lovelife.base.BaseFragment

private const val ARG_PARAM1 = "type"

class ViewPageType2Fragment : BaseFragment() {
    private var type: String? = null
    private lateinit var binding: FragmentViewPageType2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPageType2Binding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun init() {
        binding.text.text = type
    }

    override fun bindingListener() {
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            ViewPageType2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, type)
                }
            }
    }
}