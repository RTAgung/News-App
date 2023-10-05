package com.example.newsapp.data.source.remote.service

import com.example.newsapp.data.source.remote.response.DataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getArticles(
        @Query("from") fromDate: String,
        @Query("domains") domains: String = "cnn.com",
        @Query("language") language: String = "en",
        @Query("q") query: String = "",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): DataResponse
}