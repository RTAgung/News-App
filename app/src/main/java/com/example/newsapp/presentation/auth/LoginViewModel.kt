package com.example.newsapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var repository: ArticleRepository

    fun saveLogin(email: String) {
        viewModelScope.launch {
            repository.saveLogin(email)
        }
    }
}