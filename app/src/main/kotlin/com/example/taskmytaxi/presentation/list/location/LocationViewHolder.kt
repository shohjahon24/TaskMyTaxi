package com.example.taskmytaxi.presentation.list.location

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.ItemExpandableBinding
import com.example.taskmytaxi.databinding.ItemInnerLocationBinding
import com.example.taskmytaxi.databinding.ItemLocationBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.presentation.list.LocationListener

class LocationViewHolder(
    private val binding: ItemExpandableBinding
) :
    RecyclerView.ViewHolder(binding.root), LocationListener {

    var listener: ((Location) -> Unit)? = null

    fun bind(location: Locations) {
        val parentBinding = ItemLocationBinding.bind(binding.expandable.parentLayout)
        parentBinding.apply {
            tvAddress.text = location.data[0].address
            tvSubAddress.text = location.data[0].formattedAddress
        }
        val childBinding = ItemInnerLocationBinding.bind(binding.expandable.secondLayout)
        val adapter = InnerLocationAdapter(location.data.subList(1, location.data.size))
        childBinding.listLocation.adapter = adapter
        if (location.data.size > 1) {
            parentBinding.ivArrow.visibility = View.VISIBLE
            itemView.setOnClickListener {
                if (binding.expandable.isExpanded) {
                    binding.expandable.collapse()
                    parentBinding.ivArrow.setImageResource(R.drawable.ic_arrow_down)
                } else {
                    parentBinding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
                    binding.expandable.expand()
                }
            }
            adapter.listener = this
        } else {
            parentBinding.ivArrow.visibility = View.GONE
            itemView.setOnClickListener { listener?.invoke(location.data[0]) }
            binding.expandable.collapse()
        }
    }

    override fun onItemClick(location: Location) {
        listener?.invoke(location)
    }
}