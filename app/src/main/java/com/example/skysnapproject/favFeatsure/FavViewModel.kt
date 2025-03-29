package com.example.skysnapproject.favFeatsure

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val repository: RepositoryInterface) : ViewModel() {

    private val _favPlaces = MutableStateFlow<List<Place>>(emptyList())
    val favPlaces: StateFlow<List<Place>> = _favPlaces.asStateFlow()

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = mutableMessage

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavPlace().collect { places ->
                _favPlaces.emit(places)
            }
        }
    }


    fun deletePlace(place: Place) {
        viewModelScope.launch {
            try {
                val result = repository.removePlace(place)
                if (result > 0) {
                    mutableMessage.postValue("Deleted successfully")
                    Log.i("DeletePlace", "deletePlace: deleted sussefully ")

                } else {
                    mutableMessage.postValue("not found product")
                    Log.i("DeletePlace", "deletePlace: cant' delete ")
                }

            } catch (ex: Exception) {
                mutableMessage.postValue("Error: ${ex.message}")

            }

        }
    }

    class FavViewModelFactory(
        private val repository: RepositoryInterface) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavViewModel(
                    repository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

