package com.example.weatherapp.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.PersistenceManager
import com.example.weatherapp.network.NetworkResponse
import com.example.weatherapp.network.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val sharedPreferences: PersistenceManager
) : ViewModel() {

    private val _weatherInfo = MutableStateFlow(WeatherUIData())
    val weatherInfo: StateFlow<WeatherUIData> = _weatherInfo

    init {
        // Load the last searched city from local storage, if available
        val lastSearchedCity = sharedPreferences.getLastSearchedCity()
        if (lastSearchedCity != null) {
            fetchWeather(lastSearchedCity)
        }
    }

    fun fetchWeather(location: String) {
        viewModelScope.launch {
            getWeather(location)
                .onEach { result ->
                    if (result is NetworkResponse.Success) {
                        sharedPreferences.saveLastSearchedCity(location)
                    }
                    _weatherInfo.value = WeatherUIData(
                        name = result.data?.name.orEmpty(),
                        temp = result.data?.main?.temp.toString(),
                        description = result.data?.weather?.firstOrNull()?.description.orEmpty(),
                        icon = result.data?.weather?.firstOrNull()?.icon.orEmpty()
                    )
                }.launchIn(this)
        }
    }

    fun fetchWeatherForCurrentLocation(latitude: Float, longitude: Float) {
        getWeatherByLocation(latitude, longitude)
            .onEach { result ->
                if (result is NetworkResponse.Success) {
                    sharedPreferences.saveLastSearchedLocation(latitude, longitude)
                }
                _weatherInfo.value = WeatherUIData(
                    name = result.data?.name.orEmpty(),
                    temp = result.data?.main?.temp.toString(),
                    description = result.data?.weather?.firstOrNull()?.description.orEmpty(),
                    icon = result.data?.weather?.firstOrNull()?.icon.orEmpty()
                )
            }.launchIn(viewModelScope)
    }

    private fun getWeather(location: String) = flow {
        emit(NetworkResponse.Loading())
        val response = repository.getWeather(location)
        emit(NetworkResponse.Success(response))
    }.catch { e ->
        emit(NetworkResponse.Error(e))
    }.flowOn(Dispatchers.IO)

    private fun getWeatherByLocation(latitude: Float, longitude: Float) = flow {
        emit(NetworkResponse.Loading())
        val response = repository.getWeatherByLocation(latitude, longitude)
        emit(NetworkResponse.Success(response))
    }.catch { e ->
        emit(NetworkResponse.Error(e))
    }.flowOn(Dispatchers.IO)
}

