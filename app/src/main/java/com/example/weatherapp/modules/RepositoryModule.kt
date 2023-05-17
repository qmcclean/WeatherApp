package com.example.weatherapp.modules

import com.example.weatherapp.network.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { WeatherRepository(get()) }
}
