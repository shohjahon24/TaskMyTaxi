package com.example.taskmytaxi.presentation.list.stop_point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.databinding.ItemStopPointBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.presentation.list.StopPointListener
import javax.inject.Inject

class StopPointAdapter @Inject constructor() : RecyclerView.Adapter<StopPointViewHolder>() {

    private var data: ArrayList<Location> = ArrayList()

    var listener: StopPointListener? = null

    fun addData(location: Location) {
        data.add(location)
        notifyItemInserted(data.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopPointViewHolder {
        val binding = ItemStopPointBinding.inflate(LayoutInflater.from(parent.context))
        return StopPointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StopPointViewHolder, position: Int) {
        holder.bind(data[position])
        holder.editListener = {
            listener?.onEditClick(it)
        }
        holder.removeListener = {
            data.removeAt(it)
            notifyItemRemoved(it)
            listener?.onRemoveClick()
        }
    }

    override fun getItemCount(): Int = data.size
}