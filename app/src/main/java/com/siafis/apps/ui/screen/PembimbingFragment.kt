package com.siafis.apps.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.siafis.apps.databinding.FragmentPembimbingBinding


class PembimbingFragment : Fragment() {

    private val binding:FragmentPembimbingBinding by lazy {
        FragmentPembimbingBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


}