package com.example.weatherdemo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.weatherdemo.R
import com.example.weatherdemo.data.api.RetrofitInstance
import com.example.weatherdemo.data.models.WeatherResponse
import com.example.weatherdemo.data.repository.WeatherRepository
import com.example.weatherdemo.viewmodel.WeatherViewModel
import com.example.weatherdemo.viewmodel.WeatherViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(WeatherRepository(RetrofitInstance.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ FIX DI SINI SAJA
        val username = intent.getStringExtra("username") ?: "Guest"

        val tvWelcomeMessage = findViewById<TextView>(R.id.tvWelcomeMessage)
        tvWelcomeMessage.text = "Welcome, $username!"

        // FAB chat → kirim username ke ChatActivity
        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            val i = Intent(this, ChatActivity::class.java)
            i.putExtra("username", username)
            startActivity(i)
        }

        initViews()
        setupObservers()
        viewModel.fetchWeatherData("London")
    }

    private fun initViews() {
        val btnSearch = findViewById<MaterialButton>(R.id.btnSearch)
        val etSearch = findViewById<EditText>(R.id.etSearch)

        btnSearch.setOnClickListener {
            val location = etSearch.text.toString().trim()
            if (location.isNotEmpty()) {
                hideKeyboard()
                viewModel.fetchWeatherData(location)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.weatherData.observe(this, Observer { weatherData ->
            weatherData?.let { updateWeatherUI(it) }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            findViewById<ProgressBar>(R.id.progressBar).visibility =
                if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            val tvError = findViewById<TextView>(R.id.tvError)
            if (errorMessage.isNotEmpty()) {
                tvError.text = errorMessage
                tvError.visibility = View.VISIBLE
                findViewById<MaterialCardView>(R.id.weatherCard).visibility = View.GONE
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                tvError.visibility = View.GONE
            }
        })
    }

    private fun updateWeatherUI(weatherResponse: WeatherResponse) {
        val weatherCard = findViewById<MaterialCardView>(R.id.weatherCard)
        val tvLocation = findViewById<TextView>(R.id.tvLocation)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)
        val tvFeelsLike = findViewById<TextView>(R.id.tvFeelsLike)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWind = findViewById<TextView>(R.id.tvWind)

        tvLocation.text = "${weatherResponse.location.name}, ${weatherResponse.location.country}"
        tvTemperature.text = "${weatherResponse.current.temp_c}°C"
        tvCondition.text = weatherResponse.current.condition.text
        tvFeelsLike.text = "${weatherResponse.current.feelslike_c}°C"
        tvHumidity.text = "${weatherResponse.current.humidity}%"
        tvWind.text = "${weatherResponse.current.wind_kph} km/h"

        val condition = weatherResponse.current.condition.text.lowercase()
        val mainLayout = findViewById<LinearLayout>(R.id.mainLayout)

        when {
            condition.contains("clear") || condition.contains("sunny") -> {
                weatherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sunny))
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.sunny_background))
            }
            condition.contains("rain") -> {
                weatherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.rainy))
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.rainy_background))
            }
            condition.contains("cloudy") -> {
                weatherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.cloudy))
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.cloudy_background))
            }
            condition.contains("snow") -> {
                weatherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.snowy))
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.snowy_background))
            }
            else -> {
                weatherCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.default_weather))
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.default_weather_background))
            }
        }

        weatherCard.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
