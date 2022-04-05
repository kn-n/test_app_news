package com.example.test_app_news.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test_app_news.SQLite.SQLiteHelper
import com.example.test_app_news.model.Category
import com.example.test_app_news.ui.main.CategoriesFragment

@SuppressLint("StaticFieldLeak")
class CategoriesViewModel(application: Application): AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val helper = SQLiteHelper(context)

    private val _getAllCategories = MutableLiveData<ArrayList<Category>>().apply {
        value = helper.getCategories()
    }
    var getAllCategories: MutableLiveData<ArrayList<Category>> = _getAllCategories

    fun addCategory(name:String){
        val category = Category(name.lowercase())
        val allCategories = helper.getCategories()
        if (allCategories.contains(category)) Toast.makeText(context, "Category already exist", Toast.LENGTH_SHORT).show()
        else {
            helper.insertCategory(category)
            _getAllCategories.value = helper.getCategories()
        }
    }

    fun editCategory(name: String, newName: String){
        helper.updateCategory(name, newName)
        _getAllCategories.value = helper.getCategories()
    }

    fun deleteCategory(name: String){
        helper.deleteCategory(name)
        _getAllCategories.value = helper.getCategories()
    }

}