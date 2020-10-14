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

class AtletAdapter : RecyclerView.Adapter<AtletAdapter.AtletHolder>() {
    private var listGetAtlet: MutableList<Atlet> = ArrayList()
    private var onItemClick: OnItemClick? = null
    private var sort: Boolean = false

    fun itemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    fun replaceAll(items: MutableList<Atlet>, sort: Boolean) {
        listGetAtlet.clear()
        listGetAtlet = items
        this.sort = sort
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AtletHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_rv_atlet, viewGroup, false)
        return AtletHolder(view)
    }

    override fun onBindViewHolder(holder: AtletHolder, position: Int) {
        holder.bind(listGetAtlet[position])
    }

    override fun getItemCount(): Int {
        return listGetAtlet.size
    }

    inner class AtletHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nama: TextView = itemView.findViewById(R.id.txtNama)
        private var umur: TextView = itemView.findViewById(R.id.txtUmur)
        private var gender: TextView = itemView.findViewById(R.id.txtKelamin)
        private var nilai: TextView = itemView.findViewById(R.id.txtNilai)
        private var tambah: MaterialButton = itemView.findViewById(R.id.btnPenilaian)
        private var detail: MaterialButton = itemView.findViewById(R.id.btnDetail)
        private var delete: ImageButton = itemView.findViewById(R.id.imgDelete)
        fun bind(item: Atlet) {
            nama.text = item.nama
            umur.text = item.umur.toString()
            gender.text = item.gender
            if (sort) {
                nilai.visible()
            } else {
                nilai.gone()
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
