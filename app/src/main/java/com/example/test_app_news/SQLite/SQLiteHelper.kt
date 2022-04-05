package com.example.test_app_news.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.test_app_news.model.Category
import com.example.test_app_news.model.News
import com.example.test_app_news.model.NewsSQLite

val DATABASE_NAME = "DataBase"
val TABLE_NAME_NEWS = "News"
val TABLE_NAME_CATEGORIES = "Categories"
val NEWS_COL_ID = "Id"
val NEWS_COL_CATEGORY = "Category"
val NEWS_COL_SOURCE = "Source"
val NEWS_COL_TITLE = "Title"
val NEWS_COL_DESCRIPTION = "Description"
val NEWS_COL_URL = "Url"
val NEWS_COL_URL_TO_IMG = "UrlToImg"
val NEWS_COL_DATE_AND_TIME = "DateAndTime"
val CATEGORIES_COL_ID = "Id"
val CATEGORIES_COL_NAME = "Name"

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableNews = "CREATE TABLE " + TABLE_NAME_NEWS + " (" +
                NEWS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NEWS_COL_CATEGORY+ " VARCHAR(256), " +
                NEWS_COL_SOURCE+ " VARCHAR(256), " +
                NEWS_COL_TITLE+ " VARCHAR(256), " +
                NEWS_COL_DESCRIPTION+ " VARCHAR(256), " +
                NEWS_COL_URL + " VARCHAR(256), " +
                NEWS_COL_URL_TO_IMG + " VARCHAR(256), " +
                NEWS_COL_DATE_AND_TIME + " VARCHAR(256))"
        db?.execSQL(createTableNews)

        val createTableCategories = "CREATE TABLE " + TABLE_NAME_CATEGORIES + " (" +
                CATEGORIES_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CATEGORIES_COL_NAME + " VARCHAR(256))"
        db?.execSQL(createTableCategories)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertCategory(category: Category): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(CATEGORIES_COL_NAME, category.name)
        val success = db.insert(TABLE_NAME_CATEGORIES, null, cv)
        db.close()
        Log.d("SQL", "$success insertCategory")
        return "$success".toInt() != -1
    }

    fun deleteCategory(name: String) {
        val db = this.writableDatabase
        val value = arrayOf(name)
        val success = db.delete(
            TABLE_NAME_CATEGORIES,
            "$CATEGORIES_COL_NAME = ?",
            value
        ).toLong()
        db.close()
        Log.d("SQL", "$success deleteCategory")
    }

    fun updateCategory(name: String, newName: String){
        val db = this.writableDatabase
        val value = arrayOf(name)
        val cv = ContentValues()
        cv.put(CATEGORIES_COL_NAME, newName)
        val success = db.update(TABLE_NAME_CATEGORIES, cv, "$CATEGORIES_COL_NAME = ?", value).toLong()
        db.close()
        Log.d("SQL", "$success updateCategory")
    }

    @SuppressLint("Range")
    fun getCategories(): ArrayList<Category> {
        val list: ArrayList<Category> = ArrayList()
        val db = this.readableDatabase
        val query =
            "SELECT  * FROM $TABLE_NAME_CATEGORIES"

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val category = Category(result.getString(result.getColumnIndex(CATEGORIES_COL_NAME)).toString())
                list.add(category)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        Log.d("SQL", "${list.size} getCategories")
        return list
    }

    fun insertNews(news: NewsSQLite): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(NEWS_COL_CATEGORY, news.category)
        cv.put(NEWS_COL_SOURCE, news.source)
        cv.put(NEWS_COL_TITLE, news.title)
        cv.put(NEWS_COL_DESCRIPTION, news.description)
        cv.put(NEWS_COL_URL, news.url)
        cv.put(NEWS_COL_URL_TO_IMG, news.urlToImg)
        cv.put(NEWS_COL_DATE_AND_TIME, news.publishedAt)
        val success = db.insert(TABLE_NAME_NEWS, null, cv)
        db.close()
        Log.d("SQL", "$success insertNews")
        return "$success".toInt() != -1
    }

    @SuppressLint("Range")
    fun getNews(): ArrayList<NewsSQLite> {
        val list: ArrayList<NewsSQLite> = ArrayList()
        val db = this.readableDatabase
        val query =
            "SELECT  * FROM $TABLE_NAME_NEWS"

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val news = NewsSQLite(result.getString(result.getColumnIndex(NEWS_COL_CATEGORY)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_SOURCE)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_TITLE)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_DESCRIPTION)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_URL)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_URL_TO_IMG)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_DATE_AND_TIME)).toString())
                list.add(news)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        Log.d("SQL", "${list.size} getNews")
        return list
    }

    fun deleteNews() {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME_NEWS,null,null).toLong()
        db.close()
        Log.d("SQL", "$success deleteNews")
    }

    @SuppressLint("Range")
    fun getNewsWidthCategory(category: String): ArrayList<NewsSQLite> {
        val list: ArrayList<NewsSQLite> = ArrayList()
        val db = this.readableDatabase
        val query =
            "SELECT  * FROM $TABLE_NAME_NEWS WHERE $NEWS_COL_CATEGORY = ?"
        val value = arrayOf(category)
        val result = db.rawQuery(query, value)

        if (result.moveToFirst()) {
            do {
                val news = NewsSQLite(result.getString(result.getColumnIndex(NEWS_COL_CATEGORY)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_SOURCE)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_TITLE)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_DESCRIPTION)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_URL)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_URL_TO_IMG)).toString(),
                    result.getString(result.getColumnIndex(NEWS_COL_DATE_AND_TIME)).toString())
                list.add(news)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        Log.d("SQL", "${list.size} getNews category $category")
        return list
    }

}