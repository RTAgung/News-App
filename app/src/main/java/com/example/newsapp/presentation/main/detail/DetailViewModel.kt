package com.example.newsapp.presentation.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var repository: ArticleRepository

    private var _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    fun getDetailArticle(title: String) {
        viewModelScope.launch {
            _article.value = repository.getDetailArticle(title)
        }
    }

    fun insertBookmark(article: Article) {
        viewModelScope.launch {
            repository.insertBookmark(article)
        }
        getDetailArticle(article.title)
    }

    fun deleteBookmark(title: String) {
        viewModelScope.launch {
            repository.deleteBookmark(title)
        }
        getDetailArticle(title)
    }
}