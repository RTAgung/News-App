package com.example.newsapp.data.source.remote.response

data class DataResponse(
	val totalResults: Int? = null,
	val articles: List<ArticlesItem?>? = null,
	val status: String? = null
)
