package com.example.taskmytaxi.presentation.ui.main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.repository.location.LocationRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: LocationRepository) : ViewModel() {

    val points: MutableLiveData<List<LatLng>> = MutableLiveData()

    fun getRoute(from: Point, to: Point) {
        viewModelScope.launch(IO) {
            when (val result = repo.getRoute(pointFrom = from, pointTo = to)) {
                is ClientResponse.Success -> {
                    val routes = result.result.points.map { LatLng(it.lat, it.lng) }
                    points.postValue(routes)
                }
                is ClientResponse.Error -> {
                }
            }
        }
    }
}