package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.CategoryItemBinding
import com.example.easyfood.pojo.Category

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoriesList = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)? = null

    fun setCategoryList(categoryList: List<Category>) {
        this.categoriesList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    class CategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val categoriesListPosition = categoriesList[position]

        Glide.with(holder.itemView)
            .load(categoriesListPosition.strCategoryThumb)
            .into(holder.binding.ivCategory)

        holder.binding.tvCategoryName.text = categoriesListPosition.strCategory

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoriesListPosition)
        }
    }

    override fun getItemCount() = categoriesList.size

}
