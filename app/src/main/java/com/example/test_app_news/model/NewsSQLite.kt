package com.example.test_app_news.model

data class NewsSQLite (
//    var id: Int? = null,
    var category: String,
    var source: String,
    var title: String,
    var description: String,
    var url: String,
    var urlToImg: String,
    var publishedAt: String
)