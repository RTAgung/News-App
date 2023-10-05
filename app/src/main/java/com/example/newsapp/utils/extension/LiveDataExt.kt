package com.example.newsapp.utils.extension

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.trigger() {
    value = value
}