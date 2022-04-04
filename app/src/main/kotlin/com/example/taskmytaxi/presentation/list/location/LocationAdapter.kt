package com.example.taskmytaxi.presentation.list.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.presentation.list.OnLocationClick
import javax.inject.Inject

class LocationAdapter @Inject constructor() : RecyclerView.Adapter<LocationViewHolder>() {

    private var data: List<Location> = ArrayList()

    var listener: OnLocationClick? = null

    fun setData(data: List<Location>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context))
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(data[position])
        holder.listener = {
            listener?.onItemClick(it)
        }
    }

    override fun getItemCount(): Int = data.size
}