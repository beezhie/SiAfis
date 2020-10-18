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
import com.google.firebase.firestore.Query
import com.siafis.apps.R
import com.siafis.apps.data.adapter.TanggalAdapter
import com.siafis.apps.data.model.Tanggal
import com.siafis.apps.databinding.FragmentTanggalBinding
import com.siafis.apps.ui.base.BaseFragment
import com.siafis.apps.utils.snackBar
import com.siafis.apps.utils.timeToDate
import com.siafis.apps.utils.toTimeStamp
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
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appPreference.tanggal.asLiveData().observe(viewLifecycleOwner, { guideTanggal ->
            println("GUIDE $guideTanggal")
            if (guideTanggal == null || !guideTanggal) {
                setGuideDate(
                    binding.imgAdd,
                    1,
                    "Menu Tambah",
                    "Menu ini digunakan untuk menambahkan tanggal pendataan atlet"
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
        binding.imgProfil.setOnClickListener {
            findNavController().navigate(R.id.action_tanggalFragment_to_profilFragment, null, getNavOptions())
        }
        tanggalAdapter.itemClick(object : TanggalAdapter.OnItemClick {
            override fun onItemClicked(item: Tanggal) {
                val bundle = Bundle()
                bundle.putString(AtletFragment.ID, item.id)
                findNavController().navigate(R.id.action_tanggalFragment_to_atletFragment, bundle, getNavOptions())
            }

            override fun onItemDelete(item: Tanggal) {
                db.collection("afis")
                    .document(auth.currentUser!!.uid)
                    .collection("tanggal")
                    .document(item.id)
                    .delete()
                    .addOnSuccessListener {
                        binding.root.snackBar("Document succes deleted")
                    }.addOnFailureListener {
                        binding.root.snackBar("Document failure deleted : $it")
                    }
            }
        })
    }

    private fun setGuideDate(view: View, posisi: Int, title: String, description: String) {
        GuideView.Builder(requireContext())
            .setTitle(title)
            .setContentText(description)
            .setContentTextSize(12)//optional
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view)
            .setDismissType(DismissType.anywhere)
            .setGuideListener {
                when (posisi) {
                    1 -> setGuideDate(
                        binding.imgProfil,
                        2,
                        "Menu Profil",
                        "Menu ini digunakan untuk menampilkan profil pengembang aplikasi"
                    )
                    else -> lifecycleScope.launch { appPreference.saveTanggal(true) }
                }
            }
            .build()
            .show()
    }

    private fun addTanggal(tanggal: Date?) {
        val date = hashMapOf(
            "tanggal" to tanggal?.time
        )
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .add(date)
            .addOnSuccessListener {
                binding.root.snackBar("Document added")
            }
            .addOnFailureListener { e ->
                binding.root.snackBar("Error adding document : $e")
            }
    }

    private fun getTanggal() {
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .orderBy("tanggal", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    binding.root.snackBar("Error document : $e")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
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