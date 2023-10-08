package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.repository.ArticleRepository
import com.example.newsapp.data.source.local.LocalDataSource
import com.example.newsapp.data.source.local.database.ArticleDatabase
import com.example.newsapp.data.source.local.preferences.ArticlePreferences
import com.example.newsapp.data.source.remote.RemoteDataSource
import com.example.newsapp.data.source.remote.service.ApiConfig

object Injection {
    fun provideRepository(context: Context): ArticleRepository {
        val articlePreferences = ArticlePreferences.getInstance(context)
        val localDataSource =
            LocalDataSource.getInstance(ArticleDatabase.getDatabase(context), articlePreferences)
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.createApiService())
        return ArticleRepository.getInstance(localDataSource, remoteDataSource)
    }
}