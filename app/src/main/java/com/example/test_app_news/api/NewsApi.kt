package com.example.test_app_news.api

import com.example.test_app_news.model.News
import com.example.test_app_news.model.NewsResponse
import com.example.test_app_news.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable
import java.util.*

interface NewsApi {

    @GET("top-headlines")
    fun searchForNews(
        @Query("country") country: String = "ru",
        @Query("category") category: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = API_KEY
    ): Observable<NewsResponse>

//    @GET("v2/everything")
//    suspend fun searchForCategoryNews(
//        @Query("category")
//        category: String,
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey")
//        apiKey: String = API_KEY
//    ): Response<NewsResponse>

}