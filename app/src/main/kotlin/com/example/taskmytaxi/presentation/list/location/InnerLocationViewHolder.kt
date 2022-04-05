package com.example.taskmytaxi.presentation.list.location

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.ItemExpandableBinding
import com.example.taskmytaxi.databinding.ItemInnerLocationBinding
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations

class InnerLocationViewHolder(private val binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: ((Location) -> Unit)? = null

    fun bind(location: Location) {
        binding.apply {
            tvAddress.text = location.address
            tvSubAddress.text = location.formattedAddress
            binding.ivIcon.setImageResource(R.drawable.ic_point_blue)
        }
        itemView.setOnClickListener {
            listener?.invoke(location)
        }
    }
}