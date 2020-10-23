package com.siafis.apps.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentIntroBinding
import com.siafis.apps.ui.base.BaseFragment
import kotlinx.coroutines.launch
import kotlin.math.abs


class IntroFragment : BaseFragment() {
    private val binding: FragmentIntroBinding by lazy {
        FragmentIntroBinding.inflate(layoutInflater)
    }
    private val content = listOf(
        listOf(
            "SI-AFIS",
            "Vertical Jump Tes",
            "Hexagonal Obstacle Tes",
            "Multistage Fitness Tes",
            "Hand Wall Toss",
            "Push Up Tes",
            "Tes Akselerasi 35 Meter"
        ),
        listOf(
            R.drawable.voly,
            R.drawable.vertical,
            R.drawable.hexagonal,
            R.drawable.multistage,
            R.drawable.handwall,
            R.drawable.pushup, R.drawable.akselerasi
        ),
        listOf(
            "Aplikasi Analisis fisik Atlet cabang olahraga Bola Voli",
            "Vertical Jump atau lompatan vertikal adalah tindakan melompat ke atas ke udara. Ini adalah latihan yang efektif untuk membangun daya tahan dan daya ledak. Ini juga merupakan tes standar untuk mengukur output daya atlet.",
            "Hexagonal tes adalah tes kelincahan dengan melompat dalam berbagai arah, dengan kecepatan dan juga tes kemampuan untuk bergerak cepat sekaligus menjaga keseimbangan dan konsentrasi. Tes ini mengharuskan atlet untuk melakukan serangkaian dua kaki bolak-balik melompati sisi sebuah segi enam.",
            "Multistage Fitness Test adalah salah satu cara untuk mengetahui tingkat kebugaran jasmani seseorang. Biasanya tes MFT ini dilakukan pada olahraga bola basket, yang ditujukan untuk mengetahui kebugaran jasmani atlet serta wasitnya.",
            "Belum tahu",
            "Push up adalah salah satu macam bentuk tes untuk mengukur kekuatan otot lengan."
        ,"Akselerasi adalah suatu latihan yang di dalamnya berangsur-angsur adanya suatu kreasi atau jeda lari cepat dari jogging untuk jalan diakhiri dengan lari cepa"
        )
    )

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

    private fun setupUI() {
        val pagerAdapter = ScreenSlidePagerAdapter(this,content)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setPageTransformer(DepthPageTransformer())

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { _, _ -> }.attach()
    }

    private fun setupAction() {
        binding.btnSkip.setOnClickListener {
            lifecycleScope.launch { appPreference.saveIntro(true) }
            if (auth.currentUser == null) {
                findNavController().navigate(R.id.action_introFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_introFragment_to_mainFragment)
            }
        }
    }

    class ScreenSlidePagerAdapter(
        fragment: Fragment,
        val content:List<List<Any>>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = content[0].size

        override fun createFragment(position: Int): Fragment {
            val fragment = ContentIntroFragment()
            fragment.arguments = Bundle().apply {
                putString(ContentIntroFragment.TITLE,content[0][position].toString())
                putInt(ContentIntroFragment.IMAGE, content[1][position] as Int)
                putString(ContentIntroFragment.DESCRIPTION,content[2][position].toString())
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