package com.example.weatherdemo.data.repository

import com.example.weatherdemo.data.api.WeatherApiService
import com.example.weatherdemo.data.models.WeatherResponse
import com.example.weatherdemo.utils.Result
import retrofit2.Response

/**
 * Repository class that acts as a single source of truth for weather data
 * Handles data operations and API calls
 */
class WeatherRepository(private val apiService: WeatherApiService) {

    /**
     * Fetches weather data from API and returns a Result wrapper
     * @param location The location to get weather for
     * @return Result object containing either success or error
     */
    suspend fun getWeatherData(location: String): Result<WeatherResponse> {
        return try {
            // Make API call
            val response: Response<WeatherResponse> = apiService.getCurrentWeather(
                key = "YOUR_API_KEY", // API key
                query = location
            )

            // Check if response is successful
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    Result.Success(weatherResponse)
                } else {
                    Result.Error(Exception("Empty response body"))
                }
            } else {
                Result.Error(Exception("API call failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
