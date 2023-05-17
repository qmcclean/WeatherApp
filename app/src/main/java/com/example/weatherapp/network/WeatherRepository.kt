package com.example.weatherapp.network

import com.example.weatherapp.network.response.WeatherResponse

class WeatherRepository(private val api: WeatherAPI) {
    suspend fun getWeather(location: String): WeatherResponse {
        return api.getWeather(location).body() ?: throw Exception("Failed to fetch weather")
    }

    suspend fun getWeatherByLocation(lat: Float, lon: Float): WeatherResponse {
        return api.getWeatherByLocation(lat, lon).body() ?: throw Exception("Failed to fetch weather")
    }
}
