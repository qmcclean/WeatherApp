package com.example.weatherapp

import android.content.Context

private const val PREFS_NAME = "weather_app_prefs"
private const val LAST_SEARCHED_CITY = "last_searched_city"
private const val LAST_SEARCHED_LATITUDE = "last_searched_latitude"
private const val LAST_SEARCHED_LONGITUDE = "last_searched_longitude"

class PersistenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLastSearchedCity(city: String) {
        sharedPreferences.edit().putString(LAST_SEARCHED_CITY, city).apply()
    }

    fun getLastSearchedCity(): String? {
        return sharedPreferences.getString(LAST_SEARCHED_CITY, null)
    }

    fun saveLastSearchedLocation(latitude: Float, longitude: Float) {
        sharedPreferences.edit().apply {
            putFloat(LAST_SEARCHED_LATITUDE, latitude)
            putFloat(LAST_SEARCHED_LONGITUDE, longitude)
        }.apply()
    }
}
