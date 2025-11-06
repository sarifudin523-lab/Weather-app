package com.example.weatherdemo.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherdemo.R
import com.example.weatherdemo.data.api.RetrofitInstance
import com.example.weatherdemo.data.models.WeatherResponse
import com.example.weatherdemo.data.repository.WeatherRepository
import com.example.weatherdemo.viewmodel.WeatherViewModel
import com.example.weatherdemo.viewmodel.WeatherViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

/**
 * Main Activity class that handles UI and user interactions
 * Observes ViewModel LiveData and updates UI accordingly
 */
class MainActivity : AppCompatActivity() {

    // Initialize ViewModel using viewModels delegate
    private val viewModel: WeatherViewModel by viewModels {
        // Create ViewModel with repository dependency
        WeatherViewModelFactory(
            WeatherRepository(RetrofitInstance.apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initViews()

        // Set up observers for LiveData
        setupObservers()

        // Load default location weather
        viewModel.fetchWeatherData("London")
    }

    /**
     * Initializes UI components and sets up click listeners
     */
    private fun initViews() {
        val btnSearch = findViewById<MaterialButton>(R.id.btnSearch)
        val etSearch = findViewById<EditText>(R.id.etSearch)

        btnSearch.setOnClickListener {
            val location = etSearch.text.toString().trim()
            if (location.isNotEmpty()) {
                // Hide keyboard
                hideKeyboard()
                // Fetch weather data for entered location
                viewModel.fetchWeatherData(location)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets up observers for ViewModel LiveData
     * Observes weather data, loading state, and error messages
     */
    private fun setupObservers() {
        // Observe weather data changes
        viewModel.weatherData.observe(this, Observer { weatherData ->
            weatherData?.let {
                updateWeatherUI(it)
            }
        })

        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Observe error messages
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            val tvError = findViewById<TextView>(R.id.tvError)
            if (errorMessage.isNotEmpty()) {
                tvError.text = errorMessage
                tvError.visibility = View.VISIBLE
                // Hide weather card on error
                findViewById<MaterialCardView>(R.id.weatherCard).visibility = View.GONE
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                tvError.visibility = View.GONE
            }
        })
    }

    /**
     * Updates UI with weather data
     * @param weatherResponse The weather data to display
     */
    private fun updateWeatherUI(weatherResponse: WeatherResponse) {
        val weatherCard = findViewById<MaterialCardView>(R.id.weatherCard)
        val tvLocation = findViewById<TextView>(R.id.tvLocation)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)
        val tvFeelsLike = findViewById<TextView>(R.id.tvFeelsLike)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWind = findViewById<TextView>(R.id.tvWind)

        // Update UI with weather data
        tvLocation.text = "${weatherResponse.location.name}, ${weatherResponse.location.country}"
        tvTemperature.text = "${weatherResponse.current.temp_c}°C"
        tvCondition.text = weatherResponse.current.condition.text
        tvFeelsLike.text = "${weatherResponse.current.feelslike_c}°C"
        tvHumidity.text = "${weatherResponse.current.humidity}%"
        tvWind.text = "${weatherResponse.current.wind_kph} km/h"

        // Show weather card
        weatherCard.visibility = View.VISIBLE
    }

    /**
     * Hides the soft keyboard
     */
    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}