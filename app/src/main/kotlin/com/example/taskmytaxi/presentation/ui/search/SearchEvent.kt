package com.example.taskmytaxi.presentation.ui.search

sealed class SearchEvent<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : SearchEvent<T>(data)
    class Error<T>(message: String, data: T? = null) : SearchEvent<T>(data)
    class Loading<T>(data: T? = null) : SearchEvent<T>(data)
}