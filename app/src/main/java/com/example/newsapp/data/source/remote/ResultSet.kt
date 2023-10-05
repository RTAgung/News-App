package com.example.newsapp.data.source.remote

sealed class ResultSet<out T> {
    object Loading : ResultSet<Nothing>()
    data class Success<out T>(val data: T) : ResultSet<T>()
    data class Error(val code: Int, val message: String) : ResultSet<Nothing>()
}