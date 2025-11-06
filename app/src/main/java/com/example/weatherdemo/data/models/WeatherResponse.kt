package com.example.weatherdemo.data.models

/**
 * Main data class representing the entire weather response
 * We're only extracting essential fields for this simple app
 */
data class WeatherResponse(
    val location: Location,
    val current: Current
)

/**
 * Location information data class
 */
data class Location(
    val name: String,        // City name
    val country: String      // Country name
)

/**
 * Current weather conditions data class
 * We're selecting only the most relevant weather data
 */
data class Current(
    val temp_c: Double,           // Temperature in Celsius
    val condition: Condition,     // Weather condition (text and icon)
    val humidity: Int,            // Humidity percentage
    val wind_kph: Double,         // Wind speed in km/h
    val feelslike_c: Double       // "Feels like" temperature
)

/**
 * Weather condition details
 */
data class Condition(
    val text: String,        // Weather description (e.g., "Partly cloudy")
    val icon: String         // Weather icon URL
)
