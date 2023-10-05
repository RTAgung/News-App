package com.example.newsapp.presentation.adapter

import com.example.newsapp.data.model.Article

interface ArticleAdapterCallback {
    fun onItemClick(article: Article)
    fun onBookmarkClick(article: Article)
}