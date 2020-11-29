package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentMainBinding
import com.siafis.apps.ui.base.BaseFragment


class MainFragment : BaseFragment() {
    private val binding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.btnPengembang.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profilFragment, null, getNavOptions())
        }
        binding.btnPelaksanaan.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_tanggalFragment, null, getNavOptions())
        }
        binding.btnPetunjuk.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_petunjukFragment, null, getNavOptions())
        }
    }

}