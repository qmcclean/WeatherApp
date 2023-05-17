package com.example.weatherapp.network

/**
 * The NetworkResponse class is a generic class and it's designed to represent the different states of a network operation:
 * [Loading]: Represents the state where the network request has been initiated but the response is not yet received.
 * [Success]: Represents the state where the network request was successful and the data has been received.
 * [Error]: Represents the state where the network request failed due to some reason.
 */
sealed class NetworkResponse<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : NetworkResponse<T>(data)
    class Loading<T> : NetworkResponse<T>()
    class Error<T>(throwable: Throwable) : NetworkResponse<T>(error = throwable)
}
