package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.network.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid={API key}

private const val APP_ID = "appid"
private const val LOCATION = "q"
private const val LATITUDE = "lat"
private const val LONGITUDE = "lon"

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query(LOCATION) location: String,
        @Query(APP_ID) apiKey: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>

    @GET("data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query(LATITUDE) lat: Float,
        @Query(LONGITUDE) lon: Float,
        @Query(APP_ID) apiKey: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>

}
