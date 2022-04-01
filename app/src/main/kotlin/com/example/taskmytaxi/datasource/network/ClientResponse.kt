package com.example.taskmytaxi.datasource.network

sealed class ClientResponse<out T : Any> {
    data class Success<out T : Any>(val result: T) : ClientResponse<T>() {
        val success = true
    }

    data class Error(val error_code: Int) : ClientResponse<Nothing>() {
        val success = false
    }
}