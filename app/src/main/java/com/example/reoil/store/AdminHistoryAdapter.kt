package com.example.reoil.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reoil.R

class AdminHistoryAdapter(private val orderList: MutableList<Order>) : RecyclerView.Adapter<AdminHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]
        holder.tvItemStatus.text = order.status
        holder.jumlahLiter.text = order.jumlahLiter
        holder.price.text = order.price
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemStatus: TextView = itemView.findViewById(R.id.tv_item_status)
        val jumlahLiter: TextView = itemView.findViewById(R.id.jumlahLiter)
        val price: TextView = itemView.findViewById(R.id.price)
    }
}

data class Order(val status: String, val jumlahLiter: String, val price: String)