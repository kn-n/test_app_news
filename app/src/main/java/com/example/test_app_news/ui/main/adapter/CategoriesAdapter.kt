package com.example.test_app_news.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.test_app_news.R
import com.example.test_app_news.databinding.BottomSheetAddEditCategoryBinding
import com.example.test_app_news.model.Category
import com.example.test_app_news.ui.main.CategoriesFragment
import com.example.test_app_news.viewModel.CategoriesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CategoriesAdapter(private val categoriesList: ArrayList<Category>, private val fragment: CategoriesFragment)
    : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_categories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.categoryName.text = category.name
        holder.deleteBtn.setOnClickListener {
            fragment.deleteCategory(category.name)
        }
        holder.editBtn.setOnClickListener {
            fragment.editCategory(category.name)
        }
    }

    override fun getItemCount(): Int = categoriesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val editBtn: ImageButton = itemView.findViewById(R.id.edit_btn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_btn)
    }

}