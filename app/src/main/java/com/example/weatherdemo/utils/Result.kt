package com.example.weatherdemo.utils

/**
 * Sealed class representing the result of API operations
 * Can be either Success with data or Error with exception
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}