package com.app.network_helper
import java.io.Serializable

sealed class NetworkResponse<out T : Any, out U : Any>: Serializable {
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()
    object Loading : NetworkResponse<Nothing, Nothing>()
}
