package com.example.newsapp.utils.extension

import android.util.Log
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.source.local.entity.ArticleEntity
import com.example.newsapp.data.source.local.entity.ArticleWithBookmark
import com.example.newsapp.data.source.local.entity.BookmarkEntity
import com.example.newsapp.data.source.remote.response.ArticlesItem

fun ArticlesItem?.toArticle(): Article =
    Article(
        author = this?.author ?: "",
        title = this?.title ?: "",
        description = this?.description ?: "",
        urlToImage = this?.urlToImage ?: "",
        url = this?.url ?: "",
        content = this?.content ?: "",
        publishedAt = this?.publishedAt ?: "",
    )

fun ArticlesItem?.toArticleEntity(): ArticleEntity =
    ArticleEntity(
        author = this?.author ?: "",
        title = this?.title ?: "",
        description = this?.description ?: "",
        urlToImage = this?.urlToImage ?: "",
        url = this?.url ?: "",
        content = this?.content ?: "",
        publishedAt = this?.publishedAt ?: "",
    )

fun ArticleEntity.toArticle(): Article =
    Article(
        author = this.author,
        title = this.title,
        description = this.description,
        urlToImage = this.urlToImage,
        url = this.url,
        content = this.content,
        publishedAt = this.publishedAt,
    )

fun ArticleWithBookmark.toArticle(): Article {
    val data = this
    Log.d("TAGTAGTAG", "toArticle: $data")
    return Article(
        author = this.articleEntity.author,
        title = this.articleEntity.title,
        description = this.articleEntity.description,
        urlToImage = this.articleEntity.urlToImage,
        url = this.articleEntity.url,
        content = this.articleEntity.content,
        publishedAt = this.articleEntity.publishedAt,
        isBookmark = this.bookmarkEntity != null
    )
}


fun Article.toBookmarkEntity(): BookmarkEntity =
    BookmarkEntity(
        author = this.author,
        title = this.title,
        description = this.description,
        urlToImage = this.urlToImage,
        url = this.url,
        content = this.content,
        publishedAt = this.publishedAt,
        id = null
    )

fun BookmarkEntity.toArticle(): Article =
    Article(
        author = this.author,
        title = this.title,
        description = this.description,
        urlToImage = this.urlToImage,
        url = this.url,
        content = this.content,
        publishedAt = this.publishedAt,
        isBookmark = true
    )