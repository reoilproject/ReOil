package com.example.reoil.view.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reoil.databinding.ItemRowNotificationBinding

class NotificationAdapter(private val titles: Array<String>, private val descriptions: Array<String>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemRowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(titles[position], descriptions[position])
    }

    override fun getItemCount(): Int = titles.size

    class NotificationViewHolder(private val binding: ItemRowNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, description: String) {
            binding.tvItemNotif.text = title
            binding.tvItemDescription.text = description
        }
    }
}
