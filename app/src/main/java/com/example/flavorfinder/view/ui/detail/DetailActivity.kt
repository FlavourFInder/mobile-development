package com.example.flavorfinder.view.ui.detail

import android.os.Bundle
import android.util.Log
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
import com.example.flavorfinder.network.response.GetBookmarkRecipe
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.adapter.IngredientAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var repository: MealRepository
    private lateinit var userPreference: UserPreference
    private var recipeId: Int? = 0
    private var isBookmarked = false
    private var bookmarkId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        userPreference = UserPreference.getInstance(dataStore)
        repository = Injection.provideRepository(this) ?: throw IllegalStateException("Repository not initialized")

        val mealItem = intent.getParcelableExtra<MealsItem>("data")
        val bookmarkRecipe = intent.getParcelableExtra<GetBookmarkRecipe>("bookmark")

        if (mealItem != null) {
            recipeId = mealItem.idMeal?.toInt()
            setupDetailMenu(mealItem)
            checkIfBookmarked(recipeId!!)
        } else if (bookmarkRecipe != null) {
            recipeId = bookmarkRecipe.idMeal?.toInt()
            fetchAndSetupDetailMenu(bookmarkRecipe.idMeal)
            checkIfBookmarked(bookmarkRecipe.idMeal?.toInt()!!)
        } else {
            showToast("Failed to load data")
        }

        viewModel.bookmarkResult.observe(this) { result ->
            when (result) {
                is Result.Succes -> {
                    isBookmarked = !isBookmarked
                    invalidateOptionsMenu()
                    showToast(result.data)
                }
                is Result.Error -> showToast(result.error)
                is Result.Loading -> showToast("Loading...")
            }
        }

        viewModel.bookmarkId.observe(this) { bookmarkId ->
            Log.d("DetailActivity", "Bookmark ID updates: $bookmarkId")
            this.bookmarkId = bookmarkId
        }
    }

    private fun fetchAndSetupDetailMenu(mealId: String?) {
        if (mealId == null) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.lookupMeals(mealId)
                val mealItem = response.meals.firstOrNull()
                if (mealItem != null) {
                    withContext(Dispatchers.Main) {
                        setupDetailMenu(mealItem)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Meal details not found")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Lookup meal details failed")
                }
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
        val bookmarkItem = menu?.findItem(R.id.action_bookmark)
        bookmarkItem?.setIcon(if (isBookmarked) R.drawable.ic_bookmarked_circle else R.drawable.ic_bookmark_circle_background)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                Log.d("DetailActivity", "Bookmark clicked. isBookmarked: $isBookmarked, bookmarkId: $bookmarkId")
                if (isBookmarked) {
                    bookmarkId?.let {
                        viewModel.deleteBookmark(it)
                    } ?: showToast("Bookmark ID is not available")
                } else {
                    recipeId?.let {
                        viewModel.addBookmark(it)
                    } ?: showToast("Recipe ID is not available")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkIfBookmarked(recipeId: Int) {
        viewModel.checkIfBookmarked(recipeId).observe(this) { result ->
            when (result) {
                is Result.Succes -> {
                    isBookmarked = result.data != null
                    bookmarkId = result.data
                    invalidateOptionsMenu()
                }
                is Result.Error -> showToast(result.error)
                is Result.Loading -> showToast("Loading..")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
