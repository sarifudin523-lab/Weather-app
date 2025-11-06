package com.example.weatherdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherdemo.data.models.WeatherResponse
import com.example.weatherdemo.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import com.example.weatherdemo.utils.Result

/**
 * ViewModel class that prepares and manages UI-related data
 * Survives configuration changes and acts as a communication center
 * between Repository and UI
 */
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // LiveData for weather information - observed by UI
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    // LiveData for loading state - to show/hide progress bar
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData for error messages - to show error states in UI
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * Fetches weather data for a given location
     * Uses coroutines for background operations
     * @param location The city name to search for
     */
    fun fetchWeatherData(location: String) {
        _isLoading.value = true

        viewModelScope.launch {
            when (val result = repository.getWeatherData(location)) {
                is Result.Success -> {
                    _weatherData.value = result.data
                    _errorMessage.value = ""
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.message ?: "Unknown error occurred"
                }
            }
            _isLoading.value = false
        }
    }
}