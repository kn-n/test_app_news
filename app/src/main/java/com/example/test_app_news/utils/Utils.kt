package com.example.test_app_news.utils

import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

fun makeTime(date: String): String{
    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val newDate = LocalDateTime.parse(date, apiFormat)
    val dateTime = LocalDateTime.now()
    val period = Duration.between(newDate,dateTime)
    return "${period.toHours()} hours ago"
}

fun makeDate(date: String): String{
    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val newDate = LocalDateTime.parse(date, apiFormat)
    return newDate.toString()
}

fun makeOnlyDate(date: String): String{
    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val newDate = LocalDate.parse(date, apiFormat)
    return "${newDate.month} ${newDate.dayOfMonth}, ${newDate.year}"
}
