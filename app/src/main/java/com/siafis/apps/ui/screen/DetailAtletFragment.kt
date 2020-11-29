package com.siafis.apps.ui.screen

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.siafis.apps.R
import com.siafis.apps.data.adapter.HasilAdapter
import com.siafis.apps.data.model.Atlet
import com.siafis.apps.data.model.Hasil
import com.siafis.apps.databinding.FragmentDetailAtletBinding
import com.siafis.apps.utils.gone
import com.siafis.apps.utils.visible


class DetailAtletFragment(val atlet: Atlet) : BottomSheetDialogFragment() {

    private val binding: FragmentDetailAtletBinding by lazy {
        FragmentDetailAtletBinding.inflate(layoutInflater)
    }
    private lateinit var hasilAdapter: HasilAdapter

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
        binding.iconAction.gone()

        binding.nameUser.text = atlet.nama
        binding.txtJenis.text = ": ${atlet.gender}"
        binding.txtUmur.text = ": ${atlet.umur.toString()} Tahun"

        hasilAdapter = HasilAdapter()
        binding.rvHasil.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hasilAdapter
        }
        if (atlet.hasil.isEmpty()){
            binding.viewHasilTes.gone()
        }else{
            binding.viewHasilTes.visible()
            hasilAdapter.replaceAll(atlet.hasil as MutableList<Hasil>)
        }

    }

    private fun setupAction() {
        binding.btClose.setOnClickListener { dismiss() }
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