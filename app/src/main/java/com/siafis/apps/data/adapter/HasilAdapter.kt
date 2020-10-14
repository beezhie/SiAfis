package com.siafis.apps.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siafis.apps.R
import com.siafis.apps.data.model.Hasil
import com.siafis.apps.utils.gone
import com.siafis.apps.utils.visible

class HasilAdapter : RecyclerView.Adapter<HasilAdapter.HasilHolder>() {
    private var listGetHasil: MutableList<Hasil> = ArrayList()

    fun replaceAll(items: MutableList<Hasil>) {
        listGetHasil.clear()
        listGetHasil = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HasilHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_rv_hasil, viewGroup, false)
        return HasilHolder(view)
    }

    override fun onBindViewHolder(holder: HasilHolder, position: Int) {
        holder.bind(listGetHasil[position])
    }

    override fun getItemCount(): Int {
        return listGetHasil.size
    }

    inner class HasilHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nama: TextView = itemView.findViewById(R.id.txtNama)
        private var viewNilai: TextView = itemView.findViewById(R.id.viewNilai)
        private var viewTingkat: TextView = itemView.findViewById(R.id.viewTingkat)
        private var txtTingkat: TextView = itemView.findViewById(R.id.txtTingkat)
        private var hasil: TextView = itemView.findViewById(R.id.txtHasil)
        private var nilai: TextView = itemView.findViewById(R.id.txtNilai)
        fun bind(item: Hasil) {
            nama.text = item.nama
            hasil.text = item.hasil.toString()
            if(item.nama == "Multistage Fitness Tes"){
                viewTingkat.visible()
                txtTingkat.visible()
                txtTingkat.text = item.tingkat.toString()
                nilai.text = item.nilai.toString()
                viewNilai.text = "Nilai"
            }else{
                viewTingkat.gone()
                txtTingkat.gone()
                nilai.text = item.predikat.toString()
                viewNilai.text = "Predikat"
            }
        }
    }
}