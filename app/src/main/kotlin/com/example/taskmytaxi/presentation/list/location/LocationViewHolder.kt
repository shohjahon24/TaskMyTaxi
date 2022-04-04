package com.example.taskmytaxi.presentation.list.location

import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations

class LocationViewHolder(private val binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: ((Location)->Unit)? = null

    fun bind(location: Locations) {
        binding.tvAddress.text = location.data[0].address
        binding.tvSubAddress.text = location.data[0].formattedAddress
        itemView.setOnClickListener {
            listener?.invoke(location.data[0])
        }
    }
}