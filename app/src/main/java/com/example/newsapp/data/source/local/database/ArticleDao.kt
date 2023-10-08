package com.example.newsapp.data.source.local.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.newsapp.data.source.local.entity.ArticleEntity
import com.example.newsapp.data.source.local.entity.ArticleWithBookmark
import com.example.newsapp.data.source.local.entity.BookmarkEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(quote: List<ArticleEntity>)

    @Transaction
    @Query("SELECT * FROM article")
    fun getAllArticle(): PagingSource<Int, ArticleWithBookmark>

    @Query("DELETE FROM article")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM article_bookmark ORDER BY id DESC")
    fun getAllBookmark(): LiveData<List<BookmarkEntity>>

    @Query("DELETE FROM article_bookmark WHERE title = :title")
    suspend fun deleteBookmark(title: String)

    @Query("SELECT EXISTS(SELECT * FROM article_bookmark WHERE title = :title)")
    fun isBookmarked(title: String): LiveData<Boolean>
}