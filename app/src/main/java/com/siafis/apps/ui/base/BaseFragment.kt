package com.siafis.apps.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.siafis.apps.R
import com.siafis.apps.data.local.AppPreference

open class BaseFragment : Fragment() {

    protected fun getNavOptions(): NavOptions {
        return NavOptions.Builder().apply {
            setEnterAnim(R.anim.enter_from_top)
            setExitAnim(R.anim.exit_to_bottom)
            setPopEnterAnim(R.anim.enter_from_bottom)
            setPopExitAnim(R.anim.exit_to_top)
        }.build()
    }
    protected val kategori = mapOf(
        "K001" to "Vertical Jump",
        "KOO2" to "Hexagonal Obstacle Test",
        "K003" to "Multistage Fitness Tes",
        "K004" to "Hand Wall Toss",
        "K005" to "Push Up",
        "K006" to "Tes Akselerasi 35 Meter",
        "K000" to "Reset"
    )

    protected lateinit var appPreference: AppPreference
    protected lateinit var db: FirebaseFirestore
    protected lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        appPreference = AppPreference(requireContext())
    }
}