package com.example.weatherapp.features

import androidx.lifecycle.viewModelScope
import com.example.weatherapp.CoroutineTestRule
import com.example.weatherapp.PersistenceManager
import com.example.weatherapp.network.NetworkResponse
import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.network.response.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher

@ExperimentalCoroutinesApi
class WeatherViewModelTest: TestWatcher() {

    @get:Rule
    val rule = CoroutineTestRule()

    private val repository: WeatherRepository = mockk()
    private val sharedPreferences: PersistenceManager = mockk(relaxed = true)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchWeather success`() {
        val location = "San Francisco"
        val response = WeatherResponse(name = location)
        val networkResponse = NetworkResponse.Success(response).data

        if (networkResponse != null) {
            coEvery { repository.getWeather(location) } returns networkResponse
        }

        val viewModel = WeatherViewModel(repository, sharedPreferences)
        viewModel.fetchWeather(location)

        viewModel.viewModelScope.launch {
            viewModel.weatherInfo.collect { result ->
                assert(result.name == location)
            }
        }
    }

    @Test
    fun `test fetchWeatherForCurrentLocation success`() {
        val latitude = 37.7749f
        val longitude = -122.4194f
        val response = WeatherResponse(name = "San Francisco")
        val networkResponse = NetworkResponse.Success(response).data

        if (networkResponse != null) {
            coEvery { repository.getWeatherByLocation(latitude, longitude) } returns networkResponse
        }

        val viewModel = WeatherViewModel(repository, sharedPreferences)
        viewModel.fetchWeatherForCurrentLocation(latitude, longitude)

        viewModel.viewModelScope.launch {
            viewModel.weatherInfo.collect { result ->
                assert(result.name == "San Francisco")
            }
        }
    }
}

