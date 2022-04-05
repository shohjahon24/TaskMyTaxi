package com.example.taskmytaxi.presentation.list.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemExpandableBinding
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.presentation.list.LocationListener
import javax.inject.Inject

class InnerLocationAdapter @Inject constructor(private val data: List<Location>) : RecyclerView.Adapter<InnerLocationViewHolder>() {

    var listener: LocationListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerLocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerLocationViewHolder, position: Int) {
        holder.bind(data[position])
        holder.listener = {
            listener?.onItemClick(it)
        }
    }

    override fun getItemCount(): Int = data.size
}