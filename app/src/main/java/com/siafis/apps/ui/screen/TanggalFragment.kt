package com.siafis.apps.ui.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.Query
import com.siafis.apps.R
import com.siafis.apps.data.adapter.TanggalAdapter
import com.siafis.apps.data.model.Tanggal
import com.siafis.apps.databinding.FragmentTanggalBinding
import com.siafis.apps.ui.base.BaseFragment
import com.siafis.apps.utils.*
import kotlinx.coroutines.launch
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.util.*


class TanggalFragment : BaseFragment() {
    private val binding: FragmentTanggalBinding by lazy {
        FragmentTanggalBinding.inflate(layoutInflater)
    }
    private lateinit var tanggalAdapter: TanggalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appPreference.tanggal.asLiveData().observe(viewLifecycleOwner, { guideTanggal ->
            if (guideTanggal == null || !guideTanggal) {
                setGuideDate(
                    binding.imgAdd
                )
            }
        })
        setupUI()
        setupAction()
        getTanggal()
    }

    private fun setupUI() {
        tanggalAdapter = TanggalAdapter()
        binding.rvTanggal.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tanggalAdapter
        }
    }

    private fun setupAction() {
        lifecycleScope.launch {
            appPreference.saveIntro(true)
        }
        binding.imgAdd.setOnClickListener {
            val fragment = TambahTanggalFragment()
            fragment.setTargetFragment(this, 1)
            fragment.show(parentFragmentManager, fragment.tag)
        }

        tanggalAdapter.itemClick(object : TanggalAdapter.OnItemClick {
            override fun onItemClicked(item: Tanggal) {
                val bundle = Bundle()
                bundle.putString(AtletFragment.ID, item.id)
                findNavController().navigate(
                    R.id.action_tanggalFragment_to_atletFragment,
                    bundle,
                    getNavOptions()
                )
            }

            override fun onItemDelete(item: Tanggal) {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle("Hapus Tanggal")
                    setMessage("Apakah anda akan menghapus data tanggal : ${item.tanggal} ?")
                    setPositiveButton(
                        "OK"
                    ) { dialog, _ ->
                        db.collection("afis")
                            .document(auth.currentUser!!.uid)
                            .collection("tanggal")
                            .document(item.id)
                            .delete()
                            .addOnSuccessListener {
                                binding.root.snackBar("Document succes deleted")
                                dialog.dismiss()
                            }.addOnFailureListener {
                                binding.root.snackBar("Document failure deleted : $it")
                                dialog.dismiss()
                            }
                    }
                    setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                }.show()
            }
        })
    }

    private fun setGuideDate(view: View) {
        GuideView.Builder(requireContext())
            .setTitle("Menu Tambah")
            .setContentText("Menu ini digunakan untuk menambahkan tanggal pendataan atlet")
            .setContentTextSize(12)//optional
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view)
            .setDismissType(DismissType.anywhere)
            .setGuideListener {
                lifecycleScope.launch { appPreference.saveTanggal(true) }
            }
            .build()
            .show()
    }

    private fun addTanggal(tanggal: Date?) {
        dialogBuilder.show()
        val date = hashMapOf(
            "tanggal" to tanggal?.time
        )
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .add(date)
            .addOnSuccessListener {
                binding.root.snackBar("Document added")
                dialogBuilder.hide()
            }
            .addOnFailureListener { e ->
                binding.root.snackBar("Error adding document : $e")
                dialogBuilder.hide()
            }
    }

    private fun getTanggal() {
        dialogBuilder.show()
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .orderBy("tanggal", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    binding.root.snackBar("Error document : $e")
                    dialogBuilder.hide()
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val tanggal = ArrayList<Tanggal>()
                    for (doc in snapshot) {
                        doc.getLong("tanggal").let {
                            tanggal.add(Tanggal(doc.id, it!!.timeToDate()))
                        }
                    }
                    tanggalAdapter.replaceAll(tanggal)
                } else {
                    binding.root.snackBar("Data Kosong")
                }
                dialogBuilder.hide()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = data?.extras
        if (requestCode == 1) {
            val tanggal = result?.getString("tanggal")!!
            addTanggal(tanggal.toTimeStamp())
        }
    }
}