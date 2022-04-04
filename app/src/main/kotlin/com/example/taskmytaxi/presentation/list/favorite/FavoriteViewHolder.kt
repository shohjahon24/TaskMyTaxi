package com.example.taskmytaxi.presentation.list.favorite

import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location

class FavoriteViewHolder(private val binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: ((Location) -> Unit)? = null

    fun bind(location: Location) {
        binding.apply {
            tvAddress.text = location.address
            tvSubAddress.text = location.formattedAddress
            ivIcon.setImageResource(
                when (location.type) {
                    1 -> R.drawable.ic_home
                    2 -> R.drawable.ic_work
                    else -> R.drawable.ic_saved
                }
            )
        }
        itemView.setOnClickListener {
            listener?.invoke(location)
        }
    }
}