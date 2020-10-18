package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.siafis.apps.databinding.FragmentProfilBinding


class ProfilFragment : Fragment() {
    private val binding: FragmentProfilBinding by lazy {
        FragmentProfilBinding.inflate(layoutInflater)
    }

    private val titles = listOf(
        "Pengembang",
        "Pembimbing"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.pager.adapter = ViewPagerFragmentAdapter(this, titles)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private class ViewPagerFragmentAdapter(fragment: Fragment, val title: List<String>) :
        FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return PengembangFragment()
                1 -> return PembimbingFragment()
            }
            return PengembangFragment()
        }

        override fun getItemCount(): Int {
            return title.size
        }
    }
}