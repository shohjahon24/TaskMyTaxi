package com.example.taskmytaxi.presentation.list.favorite

import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location

class FavoriteViewHolder(private val binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(location: Location) {
        binding.tvAddress.text = location.address
        binding.tvSubAddress.text = location.formattedAddress
        itemView.setOnClickListener {

        }
    }
}