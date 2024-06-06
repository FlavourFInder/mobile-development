package com.example.flavorfinder.view.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityDetailBinding
import com.example.flavorfinder.di.Injection
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.adapter.IngredientAdapter

class DetailActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var repository: MealRepository
    private lateinit var userPreference: UserPreference
    private var recipeId: Int? = 0
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        userPreference = UserPreference.getInstance(dataStore)
        repository = Injection.provideRepository(this) ?: throw IllegalStateException("Repository not initialized")


        val result = intent.getParcelableExtra<MealsItem>("data")
        if (result != null) {
            recipeId = result.idMeal?.toInt()
            setupDetailMenu(result)

        } else {
            showToast("Failed to load data")
        }

        viewModel.bookmarkResult.observe(this) { result ->
            when (result) {
                is Result.Succes -> showToast("Bookmark added!")
                is Result.Error -> showToast(result.error)
                is Result.Loading -> showToast("Loading...")
            }
        }

    }

    private fun setupDetailMenu(items: MealsItem) {
        binding.apply {
            tvMenuName.text = items.strMeal
            Glide.with(this@DetailActivity)
                .load(items.strMealThumb)
                .into(ivDetail)

            val ingredients = listOf(
                items.strMeasure1 + " " + items.strIngredient1, items.strMeasure2 + " " + items.strIngredient2, items.strMeasure3 + " " + items.strIngredient3, items.strMeasure4 + " " + items.strIngredient4,
                items.strMeasure5 + " " + items.strIngredient5, items.strMeasure6 + " " + items.strIngredient6, items.strMeasure7 + " " + items.strIngredient7, items.strMeasure8 + " " + items.strIngredient8,
                items.strMeasure9 + " " + items.strIngredient9, items.strMeasure10 + " " + items.strIngredient10, items.strMeasure11 + " " + items.strIngredient11, items.strMeasure12 + " " + items.strIngredient12,
                items.strMeasure13 + " " + items.strIngredient13, items.strMeasure14 + " " + items.strIngredient14, items.strMeasure15 + " " + items.strIngredient15, items.strMeasure16 + " " + items.strIngredient16,
                items.strMeasure17 + " " + items.strIngredient17, items.strMeasure18 + " " + items.strIngredient18, items.strMeasure19 + " " + items.strIngredient19, items.strMeasure20 + " " + items.strIngredient20
            ).filterNotNull().filter { it.isNotBlank() }

            binding.rvIngredient.layoutManager = LinearLayoutManager(this@DetailActivity)
            binding.rvIngredient.adapter = IngredientAdapter(ingredients)

            tvInstruction.text = items.strInstructions
            tvCategory.text = items.strCategory
            tvCountry.text = items.strArea
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                recipeId?.let { viewModel.addBookmark(it) } ?: showToast("Recipe ID is not available")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}