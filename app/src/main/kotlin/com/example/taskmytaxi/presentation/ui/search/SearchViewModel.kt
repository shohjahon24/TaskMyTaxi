package com.example.taskmytaxi.presentation.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmytaxi.datasource.network.ClientResponse
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.domain.model.Locations
import com.example.taskmytaxi.domain.model.Point
import com.example.taskmytaxi.repository.favorite.FavoriteRepository
import com.example.taskmytaxi.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: LocationRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    val locations: MutableLiveData<SearchEvent<List<Locations>>> = MutableLiveData()
    val favorites: MutableLiveData<List<Location>> = MutableLiveData()

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

    fun getFavorites() {
        viewModelScope.launch(IO) {
            val locations = favoriteRepository.getAll()
            if (locations.isEmpty()) {
                val l = arrayListOf(
                    Location(
                        address = "34, Qorasuv 1 dahasi",
                        addressId = 0,
                        formattedAddress = "Mirzo Ulug‘bek Tumani, Toshkent, 100187, Oʻzbekiston",
                        lang = "",
                        point = Point(lat = 41.33543550010952, lng = 69.3759299442172),
                        streetId = 0,
                        type = 1
                    ),
                    Location(
                        address = "Embassy of Indonesia, 73, Yax'yo Gulomov ko'chasi",
                        addressId = 0,
                        formattedAddress = "Yashnobod Tumani, Toshkent, 100000, Oʻzbekiston",
                        lang = "",
                        point = Point(lat = 41.30744713453325, lng = 69.28950984030962),
                        streetId = 0,
                        type = 2
                    ),
                    Location(
                        address = "O‘zbekiston Anjumanlar Saroyi, Mahtumquli ko'chasi",
                        addressId = 0,
                        formattedAddress = "Yashnobod Tumani, Toshkent, 100000, Oʻzbekiston",
                        lang = "",
                        point = Point(lat = 41.309244326814145, lng = 69.28216561675072),
                        streetId = 0,
                        type = 3
                    )

                )
                favorites.postValue(l)
                favoriteRepository.insertAll(l)
            }
            else
                favorites.postValue(locations)
        }
    }
}