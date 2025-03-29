

package com.example.skysnapproject.favFeatsure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import com.example.skysnapproject.locationFeatch.LocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _favPlaces = MutableStateFlow<List<Place>>(emptyList())
    val favPlaces: StateFlow<List<Place>> = _favPlaces.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavPlace().collect { places ->
                _favPlaces.emit(places)
            }
        }
    }

    fun addPlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlace(place)
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePlace(place)
        }
    }
}

