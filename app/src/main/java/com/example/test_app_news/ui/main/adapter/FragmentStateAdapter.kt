package com.example.test_app_news.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test_app_news.ui.main.CategoriesFragment
import com.example.test_app_news.ui.main.NewsFragment
import com.example.test_app_news.viewModel.NewsViewModel

class FragmentStateAdapter(fa: FragmentActivity)
    : FragmentStateAdapter(fa){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> CategoriesFragment()
            else -> NewsFragment()
        }
    }

}