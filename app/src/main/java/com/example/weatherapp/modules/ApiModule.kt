@file:OptIn(ExperimentalSerializationApi::class)

package com.example.weatherapp.network

import com.example.weatherapp.PersistenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

private const val CONTENT_TYPE_JSON = "application/json"
private const val BASE_URL = "https://api.openweathermap.org/"

val apiModule = module {
    single { OkHttpClient.Builder().build() }
    single { Json { ignoreUnknownKeys = true } }

    single<WeatherAPI> { provideAPI() }

    single { PersistenceManager(androidContext()) }

    single<Retrofit> {
        createRetrofit(
            client = get(),
            json = get()
        )
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun createRetrofit(
    client: OkHttpClient,
    json: Json
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(json.asConverterFactory(CONTENT_TYPE_JSON.toMediaType()))
    .client(client)
    .build()

/**
 * Extension function for the Scope class using Kotlin reified type parameter.
 * It leverages the Koin dependency injection framework to provide an instance of an API interface.
 */
private inline fun <reified T> Scope.provideAPI(name: String? = null): T =
    get<Retrofit>(name?.let { named(it) }).create(T::class.java)
