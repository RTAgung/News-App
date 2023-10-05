package com.example.newsapp.data.source.remote

import com.example.newsapp.data.source.remote.service.ApiService
import com.example.newsapp.utils.Helper.getDateFor7DaysAgo

class RemoteDataSource private constructor(private val apiService: ApiService) {

    suspend fun getArticles(query: String, page: Int, pageSize: Int) =
        apiService.getArticles(
            fromDate = getDateFor7DaysAgo(),
            query = query,
            page = page,
            pageSize = pageSize,
        )

//    private fun getExceptionResponse(e: Exception): ResultSet.Error {
//        e.printStackTrace()
//        when (e) {
//            is HttpException -> {
//                val code = e.code()
//                var msg = e.message()
//                val errorMessage: String?
//                try {
//                    val jsonObj = e.response()?.errorBody()?.charStream()?.readText()
//                        ?.let { JSONObject(it) }
//                    errorMessage = jsonObj?.getString("message")
//                } catch (exception: java.lang.Exception) {
//                    return when (exception) {
//                        is UnknownHostException -> ResultSet.Error(
//                            code,
//                            "Telah terjadi kesalahan ketika koneksi ke server: ${e.message}",
//                        )
//
//                        is SocketTimeoutException -> ResultSet.Error(
//                            code,
//                            "Telah terjadi kesalahan ketika koneksi ke server: ${e.message}",
//                        )
//
//                        else -> ResultSet.Error(
//                            code,
//                            "Terjadi kesalahan pada server. errorcode : <b>$code</b>",
//                        )
//                    }
//                }
//
//                when (code) {
//                    504 -> {
//                        msg = errorMessage ?: "Error Response"
//                    }
//
//                    502, 404 -> {
//                        msg = errorMessage ?: "Error Connect or Resource Not Found"
//                    }
//
//                    400 -> {
//                        msg = errorMessage ?: "Bad Request"
//                    }
//
//                    401 -> {
//                        msg = errorMessage ?: "Not Authorized"
//                    }
//
//                    422 -> {
//                        msg = errorMessage ?: "Unprocessable Entity"
//                    }
//                }
//                return ResultSet.Error(code, msg)
//            }
//
//            else -> return ResultSet.Error(-1, e.message ?: "Unknown error occured")
//        }
//    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource {
            return instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).also { instance = it }
            }
        }
    }
}