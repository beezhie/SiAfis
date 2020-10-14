package com.siafis.apps.ui.screen

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentTambahAtletBinding
import com.siafis.apps.utils.clearInput
import com.siafis.apps.utils.inputError


class TambahAtletFragment : BottomSheetDialogFragment() {

    private val binding: FragmentTambahAtletBinding by lazy {
        FragmentTambahAtletBinding.inflate(layoutInflater)
    }
    private lateinit var mBehavior: BottomSheetBehavior<*>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(binding.root)

        setupUI()
        setupAction()

        return dialog
    }

    private fun setupUI() {
        mBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        mBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO

        binding.lytSpacer.minimumHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        showView(binding.appBarLayout, getActionBarSize())

        val itemsGender = listOf("Pria", "Wanita")
        val adapterGender = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemsGender)
        binding.etJeniskelamin.setAdapter(adapterGender)
        binding.etJeniskelamin.setText(itemsGender[0], false)

        binding.etNama.clearInput(binding.nama)
        binding.etUmur.clearInput(binding.umur)
    }

    private fun setupAction() {
        binding.btClose.setOnClickListener { dismiss() }
        binding.iconAction.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val umur = binding.etUmur.text.toString().trim()
            val gender = binding.etJeniskelamin.text.toString().trim()
            val validation = arrayOfNulls<Boolean>(2)

            validation[0] = binding.nama.inputError(
                nama, resources.getString(
                    R.string.empty_fields,
                    "Nama Lengkap"
                )
            )

            validation[1] = if (binding.umur.inputError(
                    umur, resources.getString(
                        R.string.empty_fields,
                        "Umur"
                    )
                )) {
                if (umur.toInt() in 16..19) {
                    true
                } else {
                    binding.umur.error = "Masukkan umur rentang 16 sampai 19 tahun"
                    false
                }
            } else {
                false
            }

            if(!validation.contains(false)){
                val data = hashMapOf(
                    "nama" to nama,
                    "umur" to umur.toLong(),
                    "gender" to gender
                )
                targetFragment?.onActivityResult(
                    targetRequestCode,
                    1,
                    Intent().putExtra("atlet", data)
                )
                dismiss()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val styledAttributes =
            requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }

}