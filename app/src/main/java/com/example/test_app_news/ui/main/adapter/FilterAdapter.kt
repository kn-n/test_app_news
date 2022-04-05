package com.example.test_app_news.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.test_app_news.R
import com.example.test_app_news.model.Category
import com.example.test_app_news.ui.main.NewsFragment
import com.example.test_app_news.viewModel.NewsViewModel

class FilterAdapter(private val categoriesList: ArrayList<Category>, private val newsViewModel: NewsViewModel, private val fragment: NewsFragment) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_filter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = categoriesList[position].name
        holder.checkBox.text = name
        newsViewModel.getFilter.observe(fragment.viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                if(it.contains(name)) holder.checkBox.isChecked = true
            }
            holder.checkBox.setOnClickListener {
                if (holder.checkBox.isChecked) newsViewModel.addToFilter(name)
                else newsViewModel.removeFromFilter(name)
            }
        })
    }

    override fun getItemCount(): Int = categoriesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.category)
    }
}