package com.example.newsapp.presentation.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ArticleRepository) : ViewModel() {
    private var _pagingListArticle = MutableLiveData<String>()
    val pagingListArticle: LiveData<PagingData<Article>>
        get() = _pagingListArticle.switchMap { query ->
            repository.getArticles(query).asLiveData()
        }

    val listArticle: LiveData<List<Article>>
        get() = repository.getAllBookmark()

    fun getArticles(query: String = "") {
        _pagingListArticle.value = query
    }

    fun clearLogin() {
        viewModelScope.launch {
            repository.clearLogin()
        }
    }

    fun insertBookmark(article: Article) {
        viewModelScope.launch {
            repository.insertBookmark(article)
        }
    }

    fun deleteBookmark(title: String) {
        viewModelScope.launch {
            repository.deleteBookmark(title)
        }
    }
}