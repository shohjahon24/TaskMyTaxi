package com.example.taskmytaxi.presentation.list.location

import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location

class LocationViewHolder(private val binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: ((Location)->Unit)? = null

    fun bind(location: Location) {
        binding.tvAddress.text = location.address
        binding.tvSubAddress.text = location.formattedAddress
        itemView.setOnClickListener {
            listener?.invoke(location)
        }
    }
}