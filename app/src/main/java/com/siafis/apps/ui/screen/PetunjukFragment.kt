package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.siafis.apps.databinding.FragmentPetunjukBinding
import com.siafis.apps.ui.base.BaseFragment


class PetunjukFragment : BaseFragment() {
    private val binding:FragmentPetunjukBinding by lazy {
        FragmentPetunjukBinding.inflate(layoutInflater)
    }
    val titles = listOf(
        "Vertical Jump",
        "Hexagonal Obstacle Test",
        "Multistage Fitness Tes",
        "Hand Wall Toss",
        "Push Up",
        "Tes Akselerasi 35 Meter"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                0 -> return ContentPetunjukFragment("slidesatu.html")
                1 -> return ContentPetunjukFragment("slidedua.html")
                2 -> return ContentPetunjukFragment("slidetiga.html")
                3 -> return ContentPetunjukFragment("slideempat.html")
                4 -> return ContentPetunjukFragment("slidelima.html")
                5 -> return ContentPetunjukFragment("slideenam.html")

            }
            return ContentPetunjukFragment("slidesatu.html")
        }

        override fun getItemCount(): Int {
            return title.size
        }
    }

}