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
import com.siafis.apps.databinding.FragmentFilterBinding


class FilterFragment : BottomSheetDialogFragment() {
    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
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

        val itemsTes = listOf(
            "Vertical Jump",
            "Hexagonal Obstacle Test",
            "Multistage Fitness Tes",
            "Hand Wall Toss",
            "Push Up",
            "Tes Akselerasi 35 Meter"
        )
        val adapterTes = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemsTes)
        binding.etKategori.setAdapter(adapterTes)
        binding.etKategori.setText(itemsTes[0], false)
    }

    private fun setupAction() {
        binding.btClose.setOnClickListener { dismiss() }
        binding.iconAction.setOnClickListener {
            targetFragment?.onActivityResult(
                targetRequestCode,
                4,
                Intent()
                    .putExtra("gender", binding.etJeniskelamin.text.toString())
                    .putExtra("kategori", binding.etKategori.text.toString())
            )
            dismiss()

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