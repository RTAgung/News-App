package com.example.newsapp.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.newsapp.data.repository.ArticleRepository
import com.example.newsapp.utils.extension.trigger

class WelcomeViewModel(private val repository: ArticleRepository) : ViewModel() {
    private var _loginData = MutableLiveData<String>()
    val loginData: LiveData<Map<String, Any>>
        get() = _loginData.switchMap {
            repository.getLogin().asLiveData()
        }

    fun getLogin() {
        _loginData.trigger()
    }
}