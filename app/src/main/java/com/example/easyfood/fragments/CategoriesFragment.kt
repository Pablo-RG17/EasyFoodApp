package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.easyfood.viewmodel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeCategories()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }
}