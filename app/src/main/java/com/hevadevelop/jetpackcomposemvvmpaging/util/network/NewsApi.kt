package com.hevadevelop.jetpackcomposemvvmpaging.util.network

import com.hevadevelop.jetpackcomposemvvmpaging.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val URL = "https://newsapi.org/v2/"
    }

    @GET("everything")
    suspend fun getNews(
        @Header("Authorization") authorization: String,
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): NewsResponse

}