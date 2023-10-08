package com.example.newsapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.source.ArticleRemoteMediator
import com.example.newsapp.data.source.local.LocalDataSource
import com.example.newsapp.data.source.remote.RemoteDataSource
import com.example.newsapp.utils.extension.toArticle
import com.example.newsapp.utils.extension.toBookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepository private constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    fun getArticles(query: String): Flow<PagingData<Article>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = ArticleRemoteMediator(localDataSource, remoteDataSource, query),
            pagingSourceFactory = {
                localDataSource.getAllArticles()
            }
        ).flow.map {
            it.map { articleWithBookmark ->
                articleWithBookmark.toArticle()
            }
        }
    }

    suspend fun clearLogin() {
        localDataSource.clearLogin()
    }

    suspend fun saveLogin(email: String) {
        localDataSource.saveLogin(email)
    }

    fun getLogin() = localDataSource.getLogin()

    suspend fun insertBookmark(article: Article) =
        localDataSource.insertBookmark(article.toBookmarkEntity())

    fun getAllBookmark(): LiveData<List<Article>> =
        localDataSource.getAllBookmark().map { listBookmark ->
            listBookmark.map { it.toArticle() }
        }

    suspend fun deleteBookmark(title: String) = localDataSource.deleteBookmark(title)

    fun checkBookmark(title: String) = localDataSource.checkBookmark(title)

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null

        fun getInstance(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource
        ): ArticleRepository {
            return instance ?: synchronized(this) {
                instance ?: ArticleRepository(
                    localDataSource,
                    remoteDataSource
                ).also {
                    instance = it
                }
            }
        }
    }
}