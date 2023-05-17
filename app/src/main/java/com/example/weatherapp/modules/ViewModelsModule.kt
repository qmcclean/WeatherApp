package com.example.weatherapp.modules

import com.example.weatherapp.features.WeatherViewModel
import org.koin.dsl.module

val viewModelsModule = module {
    single { WeatherViewModel(get(), get()) }
}
