package com.siafis.apps.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentRegistrationBinding
import com.siafis.apps.ui.base.BaseFragment
import com.siafis.apps.utils.inputError
import com.siafis.apps.utils.isEmailValid
import com.siafis.apps.utils.snackBar

class RegistrationFragment : BaseFragment() {

    private val binding by lazy {
        FragmentRegistrationBinding.inflate(layoutInflater)
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
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val validation = arrayOfNulls<Boolean>(2)

            validation[0] = if (binding.email.inputError(
                    email,
                    resources.getString(
                        R.string.empty_fields,
                        "Email"
                    )
                )
            ) {
                if (!email.isEmailValid()) {
                    binding.email.error = resources.getString(
                        R.string.not_valid,
                        "Email"
                    )
                    false
                } else {
                    true
                }
            } else {
                false
            }
            validation[1] = binding.password.inputError(
                pass,
                resources.getString(
                    R.string.empty_fields,
                    "Password"
                )
            )
            if (!validation.contains(false)) {
                dialogBuilder.show()
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    dialogBuilder.hide()
                    if (it.isSuccessful) {
                        binding.root.snackBar("Registrasi berhasil")
                        findNavController().navigateUp()
                    } else {
                        binding.root.snackBar(it.exception?.message)
                    }
                }.addOnFailureListener {
                    dialogBuilder.hide()
                    binding.root.snackBar(it.message)
                }
            }
        }
    }

}