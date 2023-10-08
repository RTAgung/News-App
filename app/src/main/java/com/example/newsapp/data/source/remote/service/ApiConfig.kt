package com.example.newsapp.data.source.remote.service

import com.example.newsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {

        private fun request(): Retrofit {
            val mLoggingInterceptor = HttpLoggingInterceptor()
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val mClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.let {
                        it.request().newBuilder()
                            .header("X-Api-Key", BuildConfig.API_KEY)
                            .method(it.request().method, it.request().body)
                            .build()
                    })
                }.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .apply {
                    if (BuildConfig.DEBUG)
                        addInterceptor(mLoggingInterceptor)
                }

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient.build())
                .build()

        }

        fun <T> createService(service: Class<T>): T {
            return request().create(service)
        }
    }

}