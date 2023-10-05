package com.example.newsapp.data.source.local

import androidx.room.withTransaction
import com.example.newsapp.data.source.local.database.ArticleDatabase
import com.example.newsapp.data.source.local.entity.ArticleEntity
import com.example.newsapp.data.source.local.entity.BookmarkEntity
import com.example.newsapp.data.source.local.entity.RemoteKeys

class LocalDataSource private constructor(private val database: ArticleDatabase) {
    private val articleDao by lazy {
        database.articleDao()
    }

    private val remoteKeysDao by lazy {
        database.remoteKeysDao()
    }

    suspend fun deleteRemoteKeys() {
        remoteKeysDao.deleteRemoteKeys()
    }

    suspend fun deleteAllArticles() {
        articleDao.deleteAll()
    }

    suspend fun insertKeys(keys: List<RemoteKeys>) {
        remoteKeysDao.insertAll(keys)
    }

    suspend fun insertArticles(articles: List<ArticleEntity>) {
        articleDao.insertArticle(articles)
    }

    suspend fun getRemoteKeys(title: String) = remoteKeysDao.getRemoteKeys(title)

    fun getAllArticles() = articleDao.getAllArticle()

    suspend fun getDetailArticle(title: String) = articleDao.getDetailArticle(title)

    suspend fun insertBookmark(bookmarkEntity: BookmarkEntity) =
        articleDao.insertBookmark(bookmarkEntity)

    fun getAllBookmark() = articleDao.getAllBookmark()

    suspend fun deleteBookmark(title: String) = articleDao.deleteBookmark(title)

    suspend fun withTransaction(doTransaction: suspend () -> Unit) {
        database.withTransaction {
            doTransaction()
        }
    }

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(database: ArticleDatabase): LocalDataSource {
            return instance ?: synchronized(this) {
                instance ?: LocalDataSource(database).also { instance = it }
            }
        }
    }
}