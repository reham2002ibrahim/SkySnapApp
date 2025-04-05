package com.example.skysnapproject.favFeatsure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavViewModel(
    private val repository: RepositoryInterface) : ViewModel() {

    private val _favPlaces = MutableStateFlow<List<Place>>(emptyList())
    val favPlaces: StateFlow<List<Place>> = _favPlaces.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch (Dispatchers.IO){
            repository.getFavPlace().collect { places ->
                _favPlaces.emit(places)
            }
        }
    }


    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.removePlace(place)
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        _message.emit("Deleted successfully")
                        Log.i("DeletePlace", "deletePlace: deleted sussefully ")

                    } else {
                        _message.emit("not found product")
                        Log.i("DeletePlace", "deletePlace: cant' delete ")
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    _message.emit("Error: ${ex.message}")
                }
            }

        }
    }

    fun saveLocation(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
           // val ans =
                repository.addPlace(place)
  /*          if (ans > 0) Log.i("TAG", "saveLocation: added sussefully  ")
            else Log.i("TAG", "saveLocation: can't  added  ")*/
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

