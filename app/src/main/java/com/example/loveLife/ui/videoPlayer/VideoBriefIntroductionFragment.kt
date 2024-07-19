package com.example.loveLife.ui.videoPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loveLife.base.BaseFragment
import com.example.loveLife.databinding.FragmentVideoBriefIntroductionBinding
import com.example.loveLife.ui.home.ViewPageType2Fragment

private const val ARG_PARAM1 = "id"

class VideoBriefIntroductionFragment : BaseFragment() {
    private var id: String? = null
    private lateinit var binding: FragmentVideoBriefIntroductionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBriefIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {}

    override fun initData() {}

    override fun bindingListener() { }

    companion object {
        @JvmStatic
        fun newInstance(param: String) =
            VideoBriefIntroductionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param)
                }
            }
    }
}