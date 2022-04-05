package com.example.test_app_news.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_app_news.R
import com.example.test_app_news.databinding.FragmentNewsBinding
import com.example.test_app_news.model.News
import com.example.test_app_news.ui.main.adapter.FilterAdapter
import com.example.test_app_news.ui.main.adapter.NewsAdapter
import com.example.test_app_news.utils.Constants
import com.example.test_app_news.utils.makeDate
import com.example.test_app_news.utils.makeOnlyDate
import com.example.test_app_news.viewModel.CategoriesViewModel
import com.example.test_app_news.viewModel.NewsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class NewsFragment: Fragment() {

    private lateinit var newsViewModel: NewsViewModel

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root = binding.root

        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bs.bottomSheet)

        newsViewModel.deleteNews()
        newsViewModel.searchNews()

        binding.bs.recyclerViewFilters.layoutManager = LinearLayoutManager(context)
        getNews()

        binding.filterBtn.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            newsViewModel.getAllCategories.observe(viewLifecycleOwner, Observer {
                if (it.isEmpty()) {
                    binding.bs.noFilters.visibility = View.VISIBLE
                    binding.bs.recyclerViewFilters.visibility = View.GONE
                    binding.bs.setFilter.visibility = View.GONE
                }
                else{
                    binding.bs.noFilters.visibility = View.GONE
                    binding.bs.recyclerViewFilters.visibility = View.VISIBLE
                    binding.bs.setFilter.visibility = View.VISIBLE
                    binding.bs.recyclerViewFilters.adapter = FilterAdapter(it, newsViewModel, this)
                }
            })
        }

        binding.bs.setFilter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            newsViewModel.getFilter.observe(viewLifecycleOwner, Observer { filersList ->
                if (filersList.size == 0) {
                    getNews()
                    binding.numberOfFilters.text = ""
                }
                else {
                    getNewsWithFilter(filersList)
                    binding.numberOfFilters.text = filersList.size.toString()
                }
            })
        }

        binding.recyclerViewNews.layoutManager = LinearLayoutManager(context)

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun getNewsWithFilter(filters: ArrayList<String>){
        newsViewModel.updateFilterSearch()
        newsViewModel.getNewsWithCategory.observe(viewLifecycleOwner, Observer { newsList ->
            if (newsList.isEmpty()){
                binding.noNews.visibility = View.VISIBLE
                var message = "Не найдено новостей по теме(ам)"
                for (f in filters) message+=" $f"
                binding.noNews.text = message
                binding.date.visibility = View.GONE
            }else{
                binding.noNews.visibility = View.GONE
                binding.date.visibility = View.VISIBLE
            }
            newsList.sortByDescending { makeDate(it.publishedAt) }
            binding.recyclerViewNews.adapter = NewsAdapter(newsList, this, binding.dateTxt)
        })
    }

    private fun getNews(){
        newsViewModel.getNews.observe(viewLifecycleOwner, Observer { newsList ->
            if (newsList.isEmpty()) {
                binding.noNews.visibility = View.VISIBLE
                binding.noNews.text = getString(R.string.no_news)
                binding.date.visibility = View.GONE
            }
            else {
                binding.noNews.visibility = View.GONE
                binding.date.visibility = View.VISIBLE
                newsList.sortByDescending { makeDate(it.publishedAt) }
                binding.recyclerViewNews.adapter = NewsAdapter(newsList, this, binding.dateTxt)
            }
        })
    }

    override fun onPause() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        newsViewModel.updateFilter()
    }
}