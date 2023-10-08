package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.data.repository.ArticleRepository
import com.example.newsapp.data.source.local.LocalDataSource
import com.example.newsapp.data.source.local.database.ArticleDatabase
import com.example.newsapp.data.source.local.preferences.ArticlePreferences
import com.example.newsapp.data.source.remote.RemoteDataSource
import com.example.newsapp.data.source.remote.service.ApiConfig
import com.example.newsapp.data.source.remote.service.ApiService
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideService(): ApiService {
        return ApiConfig.createService(ApiService::class.java)
    }

    @Provides
    fun providePref(context: Context): ArticlePreferences {
        return ArticlePreferences.getInstance(context)
    }

    @Provides
    fun provideDatabase(context: Context): ArticleDatabase {
        return ArticleDatabase.getDatabase(context)
    }

    @Provides
    fun provideRemoteData(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource.getInstance(apiService)
    }

    @Provides
    fun provideLocalData(
        database: ArticleDatabase,
        preferences: ArticlePreferences
    ): LocalDataSource {
        return LocalDataSource.getInstance(database, preferences)
    }

    @Provides
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): ArticleRepository {
        return ArticleRepository.getInstance(localDataSource, remoteDataSource)
    }
}
