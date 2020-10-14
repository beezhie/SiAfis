package com.siafis.apps.ui.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.siafis.apps.data.adapter.AtletAdapter
import com.siafis.apps.data.model.Atlet
import com.siafis.apps.data.model.Hasil
import com.siafis.apps.databinding.FragmentAtletBinding
import com.siafis.apps.ui.base.BaseFragment
import com.siafis.apps.utils.snackBar
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class AtletFragment : BaseFragment() {

    private val binding: FragmentAtletBinding by lazy {
        FragmentAtletBinding.inflate(layoutInflater)
    }
    private var id: String = ""
    private lateinit var atletAdapter: AtletAdapter

    companion object {
        const val ID = "ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.takeIf { it.containsKey(ID) }?.apply {
            id = getString(ID)!!
            setupUI()
            setupAction()
            getAtlet()
        }
    }

    private fun setupUI() {
        atletAdapter = AtletAdapter()
        binding.rvAtlet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = atletAdapter
        }
    }

    private fun setupAction() {
        binding.imgAdd.setOnClickListener {
            val fragment = TambahAtletFragment()
            fragment.setTargetFragment(this, 1)
            fragment.show(parentFragmentManager, fragment.tag)
        }

        atletAdapter.itemClick(object : AtletAdapter.OnItemClick {
            override fun onItemClicked(item: Atlet, isView: Boolean) {
                if (isView) {
                    val fragment = DetailAtletFragment(item)
                    fragment.setTargetFragment(this@AtletFragment, 3)
                    fragment.show(parentFragmentManager, fragment.tag)
                } else {
                    val fragment = TambahPenilaianFragment(item.id!!, item.gender)
                    fragment.setTargetFragment(this@AtletFragment, 2)
                    fragment.show(parentFragmentManager, fragment.tag)
                }
            }

            override fun onItemDelete(item: Atlet) {
                db.collection("afis")
                    .document(auth.currentUser!!.uid)
                    .collection("tanggal")
                    .document(id)
                    .collection("atlet")
                    .document(item.id!!)
                    .delete()
                    .addOnSuccessListener {
                        binding.root.snackBar("Atlet succes deleted")
                    }.addOnFailureListener {
                        binding.root.snackBar("Atlet failure deleted : $it")
                    }
            }

        })
    }

    private fun addAtlet(atlet: Serializable) {
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .document(id)
            .collection("atlet")
            .add(atlet)
            .addOnSuccessListener {
                binding.root.snackBar("Document added")
            }
            .addOnFailureListener { e ->
                binding.root.snackBar("Error adding document : $e")
            }
    }

    private fun addPenilaian(atlet: String, type: String, nilai: Serializable) {
        val key = kategori.filterValues { it == type }.keys.toString()

        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .document(id)
            .collection("atlet")
            .document(atlet)
            .update(key.substring(1, key.length - 1), nilai)
            .addOnSuccessListener {
                binding.root.snackBar("Nilai added")
            }
            .addOnFailureListener { e ->
                binding.root.snackBar("Error adding nilai : $e")
            }
    }

    private fun getAtlet() {
        db.collection("afis")
            .document(auth.currentUser!!.uid)
            .collection("tanggal")
            .document(id)
            .collection("atlet")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    binding.root.snackBar("Error document : $e")
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val atlet = ArrayList<Atlet>()
                    for (doc in snapshot) {
                        val hasil = ArrayList<Hasil>()
                        kategori.forEach { (key, _) ->
                            doc[key]?.let {
                                hasil.add(doc[key, Hasil::class.java]!!)
                            }
                        }
                        atlet.add(
                            Atlet(
                                id = doc.id,
                                gender = doc["gender"].toString(),
                                nama = doc["nama"].toString(),
                                umur = doc["umur"].toString().toInt(),
                                hasil = hasil
                            )
                        )
                    }
                    atletAdapter.replaceAll(atlet, false)
                } else {
                    binding.root.snackBar("Data Kosong")
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = data?.extras
        if (requestCode == 1) {
            val atlet = result?.getSerializable("atlet")
            addAtlet(atlet!!)
        } else if (requestCode == 2) {
            val nilai = result?.getSerializable("nilai") as HashMap<*, *>
            addPenilaian(
                atlet = nilai["id"].toString(),
                type = nilai["kategori"].toString(),
                nilai = hashMapOf(
                    "nama" to nilai["kategori"].toString(),
                    "hasil" to nilai["hasiltes"],
                    "nilai" to nilai["nilai"],
                    "tingkat" to nilai["tingkat"],
                    "predikat" to nilai["predikat"]
                )
            )

        }
    }

}