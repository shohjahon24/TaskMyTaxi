package com.example.taskmytaxi.presentation.list.stop_point

import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemStopPointBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.presentation.session_manager.SessionManager

class StopPointViewHolder(private val binding: ItemStopPointBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var editListener: ((Int)->Unit)? = null

    var removeListener: ((Int)->Unit)? = null

    fun bind(location: Location) {
        binding.apply {
            tvAddress.text = location.address
            btnEdit.setOnClickListener {
                editListener?.invoke(adapterPosition)
            }
            btnRemove.setOnClickListener {
                SessionManager.locationManager.removeLocation(location)
                removeListener?.invoke(adapterPosition)
            }
        }
    }
}