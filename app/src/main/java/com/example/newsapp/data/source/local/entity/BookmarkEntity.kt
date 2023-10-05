package com.example.newsapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val author: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = "",
    val content: String = "",
)