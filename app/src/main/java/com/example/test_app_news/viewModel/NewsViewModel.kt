package com.example.test_app_news.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test_app_news.SQLite.SQLiteHelper
import com.example.test_app_news.api.RetrofitClient
import com.example.test_app_news.model.Category
import com.example.test_app_news.model.News
import com.example.test_app_news.model.NewsResponse
import com.example.test_app_news.model.NewsSQLite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class NewsViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val helper = SQLiteHelper(context)
    private var filterList: ArrayList<String> = arrayListOf()

    private val _getAllCategories = MutableLiveData<ArrayList<Category>>().apply {
        value = helper.getCategories()
    }
    var getAllCategories: MutableLiveData<ArrayList<Category>> = _getAllCategories

    private val _getFilter = MutableLiveData<ArrayList<String>>().apply {
        value = filterList
    }
    var getFilter: MutableLiveData<ArrayList<String>> = _getFilter

    fun addToFilter(name: String){
        filterList.add(name)
        _getFilter.value = filterList
    }

    fun removeFromFilter(name: String){
        filterList.remove(name)
        _getFilter.value = filterList
    }

    fun updateFilter(){
        if (_getAllCategories.value != helper.getCategories())
        {
            val categories = helper.getCategories()
            val newFilter: ArrayList<String>  = arrayListOf()
            _getAllCategories.value = categories
            for (category in categories){
                if (filterList.contains(category.name)) newFilter.add(category.name)
            }
            filterList = newFilter
            _getFilter.value = filterList
            helper.deleteNews()
            _getNews.value = helper.getNews()
            searchNews()
        }
    }

    fun deleteNews(){
        helper.deleteNews()
        _getNews.value = helper.getNews()
    }

    fun searchNews(){
        val categories = helper.getCategories()
        for(category in categories){
            search(category.name)
        }
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var searchResult: MutableLiveData<String> = MutableLiveData()

    private fun search(query: String){
        Log.d("SN", "Ищет по $query")
        searchResult.postValue("Loading")
        compositeDisposable.add(RetrofitClient.buildService()
            .searchForNews(query,"ru")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({response -> onResponse(response, query)}, {t -> onFailure(t) }))

    }

    private fun onResponse(list: NewsResponse, category: String){
        val newsList = ArrayList(list.articles)
        for (news in newsList){
            var imgURL = ""
            var description = ""
            if (!news.urlToImage.isNullOrEmpty()) imgURL = news.urlToImage!!
            if (!news.description.isNullOrEmpty()) description = news.description!!
            val newsForBD = NewsSQLite(category, news.source.name, news.title, description, news.url, imgURL, news.publishedAt)
            helper.insertNews(newsForBD)
        }
        _getNews.value = helper.getNews()
        searchResult.postValue("Success")
        Log.d("SN", "Нашел по $category ${newsList.size}")
    }

    private fun onFailure(t: Throwable) {
        Log.d("SN", "Зафейлился $t")
        searchResult.postValue("Error")
    }

    private val _getNews = MutableLiveData<ArrayList<NewsSQLite>>().apply {
        value = helper.getNews()
    }
    var getNews: MutableLiveData<ArrayList<NewsSQLite>> = _getNews

    private val _getNewsWithCategory = MutableLiveData<ArrayList<NewsSQLite>>().apply {
        val news = ArrayList<NewsSQLite>()
        for (category in filterList){
            news.addAll(helper.getNewsWidthCategory(category))
        }
        value = news
    }
    var getNewsWithCategory: LiveData<ArrayList<NewsSQLite>> = _getNewsWithCategory

    fun updateFilterSearch(){
        val news = ArrayList<NewsSQLite>()
        for (category in filterList){
            news.addAll(helper.getNewsWidthCategory(category))
        }
        _getNewsWithCategory.value = news
    }
}