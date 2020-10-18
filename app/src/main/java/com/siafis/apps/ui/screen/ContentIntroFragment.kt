package com.siafis.apps.ui.screen

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

    companion object {
        const val TITLE = "TITLE"
        const val IMAGE = "IMAGE"
        const val DESCRIPTION = "DESCRIPTION"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.takeIf { it.containsKey("TITLE") }?.apply {
            binding.txtContent.text = getString(TITLE)
            binding.imgContent.setImageResource(getInt(IMAGE))
            binding.txtDescription.text = getString(DESCRIPTION)
        }
    }
}