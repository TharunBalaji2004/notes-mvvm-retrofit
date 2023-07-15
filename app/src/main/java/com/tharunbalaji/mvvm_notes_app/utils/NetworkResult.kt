package com.tharunbalaji.mvvm_notes_app.utils

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): NetworkResult<T>(data = data)
    class Error<T>(message: String?, data: T? = null): NetworkResult<T>(data = data, message = message)
    class Loading<T>: NetworkResult<T>()
}