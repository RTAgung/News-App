package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.repository.ArticleRepository
import com.example.newsapp.data.source.local.LocalDataSource
import com.example.newsapp.data.source.local.database.ArticleDatabase
import com.example.newsapp.data.source.preferences.ArticlePreferences
import com.example.newsapp.data.source.remote.RemoteDataSource
import com.example.newsapp.data.source.remote.service.ApiConfig

object Injection {
    fun provideRepository(context: Context): ArticleRepository {
        val localDataSource = LocalDataSource.getInstance(ArticleDatabase.getDatabase(context))
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.createApiService())
        val articlePreferences = ArticlePreferences.getInstance(context)
        return ArticleRepository.getInstance(localDataSource, remoteDataSource, articlePreferences)
    }
}