package com.example.newsapp.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ArticleWithBookmark(
    @Embedded
    val articleEntity: ArticleEntity,
    @Relation(
        parentColumn = "title",
        entityColumn = "title"
    )
    val bookmarkEntity: BookmarkEntity? = null
)