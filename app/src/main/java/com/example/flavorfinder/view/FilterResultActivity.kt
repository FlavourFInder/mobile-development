package com.example.flavorfinder.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityFilterResultBinding
import com.example.flavorfinder.network.response.FilterItem
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.view.ui.adapter.FilteredMealListAdapter
import com.example.flavorfinder.view.ui.detail.DetailActivity
import com.example.flavorfinder.di.Injection
import com.example.flavorfinder.network.repository.MealRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterResultBinding
    private lateinit var mealListAdapter: FilteredMealListAdapter
    private var repository: MealRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        repository = Injection.provideRepository(this)

        val result = intent.getStringExtra("result") ?: ""
        if (result.isNotEmpty()) {
            fetchFilterResults(result)
        }
    }

    private fun setupRecyclerView() {
        mealListAdapter = FilteredMealListAdapter()
        binding.rvFilterResult.apply {
            layoutManager = LinearLayoutManager(this@FilterResultActivity)
            adapter = mealListAdapter
        }
        mealListAdapter.setOnItemClickCallback(object : FilteredMealListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FilterItem) {
                data.idMeal?.let { lookupMealDetails(it) }
            }
        })
    }

    private fun fetchFilterResults(query: String) {
        val mealRepository = repository ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mealRepository.getFilterMenu(query)
                if (response.meals != null) {
                    val filterItems = response.meals.filterNotNull().map {
                        FilterItem(
                            idMeal = it.idMeal,
                            strMeal = it.strMeal,
                            strMealThumb = it.strMealThumb
                        )
                    }
                    withContext(Dispatchers.Main) {
                        mealListAdapter.submitList(filterItems)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@FilterResultActivity, "No meals found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fetch filter results failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FilterResultActivity, "Fetch filter results failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun lookupMealDetails(mealId: String) {
        val mealRepository = repository ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mealRepository.lookupMeals(mealId)
                val mealItem = response.meals.firstOrNull()
                if (mealItem != null) {
                    withContext(Dispatchers.Main) {
                        navigateToDetailActivity(mealItem)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@FilterResultActivity, "Meal details not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lookup meal details failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FilterResultActivity, "Lookup meal details failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToDetailActivity(mealItem: MealsItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("data", mealItem)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "FilterResultActivity"
    }
}
