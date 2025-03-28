package com.example.skysnapproject.locationFeatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface

class WeatherViewModelFactory(
    private val repository: RepositoryInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}