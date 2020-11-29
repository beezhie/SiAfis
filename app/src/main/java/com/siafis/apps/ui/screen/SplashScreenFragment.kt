package com.siafis.apps.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentSplashScreenBinding
import com.siafis.apps.ui.base.BaseFragment

class SplashScreenFragment : BaseFragment() {
    private val binding: FragmentSplashScreenBinding by lazy {
        FragmentSplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            appPreference.intro.asLiveData().observe(viewLifecycleOwner,{
                if(it == null || !it){
                    findNavController().navigate(R.id.action_splashScreenFragment_to_introFragment)
                }else{
                    if(auth.currentUser == null){
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }else{
                        findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                    }
                }
            })
        }, 3000)

    }

}