package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewmodel.MealViewModel
import com.example.easyfood.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()


        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYouTubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnFavorites.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal added to Favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYouTubeImageClick() {
        binding.ivYouTube.setOnClickListener {
            if (youtubeLink.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Enlace de YouTube no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var mealToSave: Meal? = null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observerMealDetailLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(meal: Meal) {
                onResponseCase()
                mealToSave = meal

                binding.tvCategory.text = getString(R.string.category_label, meal.strCategory)
                binding.tvArea.text = getString(R.string.area_label, meal.strArea)
                binding.tvInstructionsSteps.text = meal.strInstructions

                Log.d("MealActivity", "YouTube Link recibido: ${meal.strYoutube}")
                youtubeLink = meal.strYoutube ?: ""
            }

        })
    }

    private fun setInformationInViews() {
        val titleColor = ContextCompat.getColor(this, R.color.white)

        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.ivMealDetail)

        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(titleColor)
        binding.collapsingToolBar.setExpandedTitleColor(titleColor)
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFavorites.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.ivYouTube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFavorites.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.ivYouTube.visibility = View.VISIBLE
    }
}