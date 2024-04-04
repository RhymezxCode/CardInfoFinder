package io.rhymezxcode.cardinfofinder.util

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error<out T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<out T> : Resource<T>()
    class Empty<out T> : Resource<T>()
}