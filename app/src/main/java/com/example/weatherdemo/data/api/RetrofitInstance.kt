package com.example.weatherdemo.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Singleton object to create and provide Retrofit instance
 * Ensures we have only one instance of Retrofit throughout the app
 */
object RetrofitInstance {

    // Base URL for Weather API
    private const val BASE_URL = "http://api.weatherapi.com/v1/"

    // Create logging interceptor to see network requests in Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create OkHttpClient with logging and timeout configurations
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Lazy initialization of Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Lazy initialization of API service
    val apiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}