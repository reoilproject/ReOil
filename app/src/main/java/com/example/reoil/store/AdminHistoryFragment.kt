package com.example.reoil.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reoil.R
import com.example.reoil.databinding.ActivityCameraBinding
import com.example.reoil.databinding.FragmentAdminHistoryBinding
import com.example.reoil.databinding.FragmentAdminHomeBinding
import java.text.NumberFormat
import java.util.Locale

class AdminHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminHistoryAdapter
    private val orderList = mutableListOf<Order>()
    private lateinit var tvSaldo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_history, container, false)

        recyclerView = view.findViewById(R.id.rv_statusOrder)
        recyclerView.layoutManager = LinearLayoutManager(context)

        tvSaldo = view.findViewById(R.id.tv_saldo)

        adapter = AdminHistoryAdapter(orderList)
        recyclerView.adapter = adapter

        orderList.add(Order("Order Completed", "10 Liter", "Rp 100.000"))
        orderList.add(Order("Order Completed", "20 Liter", "Rp 200.000"))
        orderList.add(Order("Order Completed", "30 Liter", "Rp 300.000"))
        orderList.add(Order("Order Completed", "35 Liter", "Rp 450.000"))
        orderList.add(Order("Order Completed", "17 Liter", "Rp 123.000"))
        orderList.add(Order("Order Completed", "23 Liter", "Rp 190.000"))
        orderList.add(Order("Order Completed", "18 Liter", "Rp 210.000"))

        var totalSaldo = 0.0
        for(order in orderList){
            val price = order.price.replace("Rp", "").replace(".", "").toDouble()
            totalSaldo += price
        }

        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvSaldo.text = formatter.format(totalSaldo)

        adapter.notifyDataSetChanged()



        return view
    }
}

