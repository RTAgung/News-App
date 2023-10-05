package com.example.newsapp.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class WelcomeViewModel(private val repository: ArticleRepository) : ViewModel() {
    private var _loginData = MutableLiveData<Boolean>()
    val loginData: LiveData<Boolean> get() = _loginData

    fun getLogin() {
        viewModelScope.launch {
            _loginData.value = repository.getLogin().getValue("login") as Boolean
        }
    }
}