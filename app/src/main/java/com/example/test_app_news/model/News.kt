package com.example.test_app_news.model

data class News (
//    var id: Int? = null,
    var source: Source,
    var title: String,
    var description: String?,
    var url: String,
    var urlToImg: String?,
    var publishedAt: String
)