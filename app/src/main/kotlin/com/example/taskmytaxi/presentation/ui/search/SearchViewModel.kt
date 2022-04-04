package com.example.taskmytaxi.presentation.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: LocationRepository) : ViewModel() {

    val locations: MutableLiveData<SearchEvent<List<Location>>> = MutableLiveData()

    fun search(query: String) {
        viewModelScope.launch(IO) {
            locations.postValue(SearchEvent.Loading())
            when (val result = repo.search(query = query)) {
                is ClientResponse.Success -> {
                    locations.postValue(SearchEvent.Success(result.result))
                }
                is ClientResponse.Error -> {
                    locations.postValue(SearchEvent.Error(""))
                }
            }
        }
    }
}