package com.example.test_app_news.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_app_news.databinding.FragmentCategoriesBinding
import com.example.test_app_news.ui.main.adapter.CategoriesAdapter
import com.example.test_app_news.viewModel.CategoriesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root = binding.root

        categoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bs.bottomSheet)

        binding.addCategory.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(context)
        categoriesViewModel.getAllCategories.observe(viewLifecycleOwner, Observer {
            val categories = it
            binding.recyclerViewCategories.adapter = CategoriesAdapter(categories, this)
        })

        binding.bs.ready.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            if (binding.bs.editCategoryName.text.toString()
                    .isNotEmpty()
            ) categoriesViewModel.addCategory(binding.bs.editCategoryName.text.toString())
            else Toast.makeText(context, "Empty name", Toast.LENGTH_SHORT).show()
            binding.bs.editCategoryName.setText("")
        }

        return root
    }

    fun deleteCategory(name: String) {
        categoriesViewModel.deleteCategory(name)
    }

    fun editCategory(name: String) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bs.editCategoryName.setText(name)
        binding.bs.ready.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            categoriesViewModel.editCategory(name, binding.bs.editCategoryName.text.toString())
            binding.bs.editCategoryName.setText("")
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onPause() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        super.onPause()
    }
}


