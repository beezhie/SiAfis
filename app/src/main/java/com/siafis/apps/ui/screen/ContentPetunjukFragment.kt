package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.siafis.apps.databinding.FragmentContentPetunjukBinding
import com.siafis.apps.ui.base.BaseFragment


class ContentPetunjukFragment(val url: String) : BaseFragment() {
    private val binding: FragmentContentPetunjukBinding by lazy {
        FragmentContentPetunjukBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.webView.loadUrl("file:///android_asset/$url")

    }

}