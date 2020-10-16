package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.FrameMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentIntroBinding
import com.siafis.apps.ui.base.BaseFragment
import kotlin.math.abs


class IntroFragment : BaseFragment() {
    private val binding:FragmentIntroBinding by lazy {
        FragmentIntroBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupAction()
    }

    private fun setupUI(){
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setPageTransformer(DepthPageTransformer())

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { _, _ -> }.attach()
    }

    private fun setupAction(){
        binding.btnSkip.setOnClickListener {
            if(auth.currentUser == null){
                findNavController().navigate(R.id.action_introFragment_to_loginFragment)
            }else{
                findNavController().navigate(R.id.action_introFragment_to_tanggalFragment)
            }
        }
    }

    class ScreenSlidePagerAdapter(
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): Fragment {
            val fragment = ContentIntroFragment()
            fragment.arguments = Bundle().apply {
                putInt("INTRO", position)
            }
            return fragment
        }
    }


    class DepthPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 0 -> {
                        alpha = 1f
                        translationX = 0f
                        translationZ = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> {
                        alpha = 1 - position
                        translationX = pageWidth * -position
                        translationZ = -1f
                        val scaleFactor = (0.75f + (1 - 0.75f) * (1 - abs(position)))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> {
                        alpha = 0f
                    }
                }
            }
        }
    }
}