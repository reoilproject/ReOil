package com.example.reoil.store

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reoil.R
import com.example.reoil.databinding.ActivityDashboardBinding
import java.text.NumberFormat
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminHistoryAdapter
    private val orderList = mutableListOf<Order>()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExchange.setOnClickListener{
            val intent = Intent(this,ExchangeActivity::class.java)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        binding.btnTrade.setOnClickListener {
            val whatsappMessage = "Halo, saya ingin menukar minyak jelantah saya ke Reoil"
            val whatsappUrl = "https://wa.me/+6285156578449?text=$whatsappMessage"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(whatsappUrl)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.rv_statusOrder)
        recyclerView.layoutManager = LinearLayoutManager(this)


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
        binding.tvSaldo.text = formatter.format(totalSaldo)

        adapter.notifyDataSetChanged()
    }
}