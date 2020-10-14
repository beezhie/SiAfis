package com.siafis.apps.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siafis.apps.R
import com.siafis.apps.data.model.Tanggal

class TanggalAdapter : RecyclerView.Adapter<TanggalAdapter.TanggalHolder>() {
    private var listGetTanggal: MutableList<Tanggal> = ArrayList()
    private var onItemClick: OnItemClick? = null

    fun itemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

    fun replaceAll(items: MutableList<Tanggal>) {
        listGetTanggal.clear()
        listGetTanggal = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TanggalHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_rv_tanggal, viewGroup, false)
        return TanggalHolder(view)
    }

    override fun onBindViewHolder(holder: TanggalHolder, position: Int) {
        holder.bind(listGetTanggal[position])
    }

    override fun getItemCount(): Int {
        return listGetTanggal.size
    }

    inner class TanggalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tanggal: TextView = itemView.findViewById(R.id.txtTanggal)
        private var delete: ImageButton = itemView.findViewById(R.id.imgDelete)
        fun bind(item: Tanggal) {
            tanggal.text = item.tanggal
            itemView.setOnClickListener { onItemClick!!.onItemClicked(item) }
            delete.setOnClickListener { onItemClick!!.onItemDelete(item) }
        }
    }

    interface OnItemClick {
        fun onItemClicked(item: Tanggal)
        fun onItemDelete(item: Tanggal)
    }
}