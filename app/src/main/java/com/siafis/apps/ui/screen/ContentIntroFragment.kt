package com.siafis.apps.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.siafis.apps.databinding.FragmentContentIntroBinding


class ContentIntroFragment : Fragment() {

    private val binding: FragmentContentIntroBinding by lazy {
        FragmentContentIntroBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.takeIf { it.containsKey("INTRO") }?.apply {
           binding.txtContent.text = "Content ${getInt("INTRO")}"
        }
    }
}