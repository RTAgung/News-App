package com.example.newsapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ArticleRepository) : ViewModel() {
    fun saveLogin(email: String) {
        viewModelScope.launch {
            repository.saveLogin(email)
        }
    }
}