package com.siafis.apps.ui.screen

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.siafis.apps.R
import com.siafis.apps.databinding.FragmentTambahTanggalBinding
import com.siafis.apps.utils.clearInput
import com.siafis.apps.utils.inputError
import com.siafis.apps.utils.openCalender

class TambahTanggalFragment : BottomSheetDialogFragment() {

    private val binding: FragmentTambahTanggalBinding by lazy {
        FragmentTambahTanggalBinding.inflate(layoutInflater)
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

        binding.etTanggal.clearInput(binding.tanggal)
        binding.etTanggal.openCalender(requireContext())
    }

    private fun setupAction(){
        binding.btClose.setOnClickListener { dismiss() }
        binding.iconAction.setOnClickListener {
            val tanggal = binding.etTanggal.text.toString().trim()
            if (binding.tanggal.inputError(tanggal,resources.getString(
                    R.string.empty_fields,
                    "Tanggal Pendataan"
                ))){
                targetFragment?.onActivityResult(
                    targetRequestCode,
                    1,
                    Intent().putExtra("tanggal", tanggal)
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