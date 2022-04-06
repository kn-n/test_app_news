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
        @Query("q") category: String,
        @Query("country") country: String = "ru",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = API_KEY
    ): Observable<NewsResponse>
}