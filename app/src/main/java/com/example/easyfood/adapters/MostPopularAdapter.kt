package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemsBinding
import com.example.easyfood.pojo.MealsByCategory

class MostPopularAdapter() : RecyclerView.Adapter<MostPopularAdapter.PopularMealsViewHolder>() {

    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    var onLongItemClick: ((MealsByCategory) -> Unit)? = null
    var mealsList = ArrayList<MealsByCategory>()

    fun setMeals(mealList: ArrayList<MealsByCategory>) {
        this.mealsList = mealList
        notifyDataSetChanged()
    }

    class PopularMealsViewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        val mealsListPosition = mealsList[position]

        Glide.with(holder.itemView)
            .load(mealsListPosition.strMealThumb)
            .into(holder.binding.ivPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsListPosition)
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsListPosition)
            true
        }
    }

    override fun getItemCount() = mealsList.size
}