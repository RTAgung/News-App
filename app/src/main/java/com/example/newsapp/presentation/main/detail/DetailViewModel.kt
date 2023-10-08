package com.example.newsapp.presentation.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var repository: ArticleRepository

    private var _title = MutableLiveData<String>()
    val isBookmarked: LiveData<Boolean> = _title.switchMap { title ->
        repository.checkBookmark(title)
    }

    fun checkBookmark(title: String) {
        _title.value = title
    }

    fun insertBookmark(isBookmarked: Article) {
        viewModelScope.launch {
            repository.insertBookmark(isBookmarked)
        }
    }

    fun deleteBookmark(title: String) {
        viewModelScope.launch {
            repository.deleteBookmark(title)
        }
    }
}