package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.modules.repositoryModule
import com.example.weatherapp.modules.viewModelsModule
import com.example.weatherapp.network.apiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WeatherApplication)
            modules(
                apiModule,
                repositoryModule,
                viewModelsModule
            )
        }
    }
}
