package com.siafis.apps.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.siafis.apps.R
import com.siafis.apps.data.model.Atlet
import com.siafis.apps.utils.gone
import com.siafis.apps.utils.visible
import java.util.*
import kotlin.collections.ArrayList

class AtletAdapter : RecyclerView.Adapter<AtletAdapter.AtletHolder>() {
    private var listGetAtlet: MutableList<Atlet> = ArrayList()
    private var listGetAtletFilter: MutableList<Atlet> = ArrayList()
    private var onItemClick: OnItemClick? = null
    private var sort: Boolean = false
    private var kategori: String = ""

    fun itemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    fun replaceAll(items: MutableList<Atlet>) {
        listGetAtlet.clear()
        listGetAtlet = items
        listGetAtletFilter = listGetAtlet
        this.sort = false
        notifyDataSetChanged()
    }

    fun restoreItem(){
        listGetAtlet = listGetAtletFilter
        notifyDataSetChanged()
    }

    fun searchItem(item: String?) {
        val dataFilter: MutableList<Atlet> = ArrayList()
        this.sort = false
        for (data in listGetAtletFilter) {
            val nama: String = data.nama!!.toLowerCase(Locale.getDefault())
            if (nama.contains(item!!)) {
                dataFilter.add(data)
            }
        }
        listGetAtlet = dataFilter
        notifyDataSetChanged()
    }

    fun filterKategori(kategori: String, gender: String) {
        this.kategori = kategori
        if (kategori == "Reset") {
            this.sort = false
            listGetAtlet = listGetAtletFilter
        } else {
            this.sort = true
            listGetAtlet = listGetAtletFilter
            val result = listGetAtlet
                .filter { filter ->
                    filter.hasil.isNotEmpty() && filter.gender == gender && filter.hasil.any { it.nama == kategori }
                }.map {
                    it.copy(
                        copyNilai = if (it.nama == "Multistage Fitness Tes") {
                            it.hasil.find { res -> res.nama == kategori }?.nilai
                        } else {
                            it.hasil.find { res -> res.nama == kategori }?.hasil
                        }
                    )
                }
            listGetAtlet = result.sortedByDescending { it.copyNilai }.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AtletHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_rv_atlet, viewGroup, false)
        return AtletHolder(view)
    }

    override fun onBindViewHolder(holder: AtletHolder, position: Int) {
        holder.bind(listGetAtlet[position], position + 1)
    }

    override fun getItemCount(): Int {
        return listGetAtlet.size
    }

    inner class AtletHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nama: TextView = itemView.findViewById(R.id.txtNama)
        private var umur: TextView = itemView.findViewById(R.id.txtUmur)
        private var gender: TextView = itemView.findViewById(R.id.txtKelamin)
        private var nilai: TextView = itemView.findViewById(R.id.txtNilai)
        private var number: TextView = itemView.findViewById(R.id.txtNumber)
        private var tambah: MaterialButton = itemView.findViewById(R.id.btnPenilaian)
        private var detail: MaterialButton = itemView.findViewById(R.id.btnDetail)
        private var delete: ImageButton = itemView.findViewById(R.id.imgDelete)
        fun bind(item: Atlet, position: Int) {
            nama.text = item.nama
            umur.text = item.umur.toString()
            gender.text = item.gender
            if (sort) {
                nilai.visible()
                number.visible()
                number.text = position.toString()
                if (item.nama == "Multistage Fitness Tes") {
                    nilai.text = "Penilaian tes : ${item.hasil.find { it.nama == kategori }?.nilai}"
                } else {
                    nilai.text = "Penilaian tes : ${item.hasil.find { it.nama == kategori }?.hasil}"
                }
            } else {
                nilai.gone()
                number.gone()
            }
            tambah.setOnClickListener { onItemClick!!.onItemClicked(item, false) }
            detail.setOnClickListener { onItemClick!!.onItemClicked(item, true) }
            delete.setOnClickListener { onItemClick!!.onItemDelete(item) }

        }
    }

    interface OnItemClick {
        fun onItemClicked(item: Atlet, isView: Boolean)
        fun onItemDelete(item: Atlet)
    }
}
