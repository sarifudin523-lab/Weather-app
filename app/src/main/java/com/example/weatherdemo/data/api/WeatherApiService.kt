package com.example.weatherdemo.data.api

import com.example.weatherdemo.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

import retrofit2.Response

/**
 * Retrofit service interface for Weather API
 * Defines the API endpoints and request methods
 */
interface WeatherApiService {

    /**
     * Fetches current weather data for a specific location
     * @param key API key for authentication
     * @param query Location query (city name, zip code, etc.)
     * @param aqi Whether to include air quality index (yes/no)
     */
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("aqi") aqi: String = "yes"
    ): Response<WeatherResponse>
}