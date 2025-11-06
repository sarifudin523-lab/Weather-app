package com.example.weatherdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherdemo.data.repository.WeatherRepository

/**
 * Factory class for creating WeatherViewModel instances
 * Handles dependency injection for the ViewModel
 */
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the WeatherViewModel class
     * @param modelClass The class of the ViewModel to create
     * @return Created ViewModel instance
     * @throws IllegalArgumentException if the ViewModel class is unknown
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}