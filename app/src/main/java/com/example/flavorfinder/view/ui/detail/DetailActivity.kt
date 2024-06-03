package com.example.flavorfinder.view.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityDetailBinding
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.view.ui.adapter.IngredientAdapter
import com.example.flavorfinder.view.ui.adapter.MealListAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / scrollRange

            if (percentage >= 0.7) {
                binding.topAppBar.setBackgroundColor(getColor(R.color.transparent))
            } else {
                binding.topAppBar.setBackgroundColor(getColor(android.R.color.white))
            }
        }

        val result = intent.getParcelableExtra<MealsItem>("data")
        if (result != null) {
            setupDetailMenu(result)
        } else {
            showToast("Failed to load data")
        }
    }

    private fun setupDetailMenu(items: MealsItem) {
        binding.apply {
            tvMenuName.text = items.strMeal
            Glide.with(this@DetailActivity)
                .load(items.strMealThumb)
                .into(ivDetail)

            val ingredients = listOf(
                items.strIngredient1 + " " + items.strMeasure1, items.strIngredient2 + " " + items.strMeasure2, items.strIngredient3 + " " + items.strMeasure3, items.strIngredient4 + " " + items.strMeasure4,
                items.strIngredient5 + " " + items.strMeasure5, items.strIngredient6 + " " + items.strMeasure6, items.strIngredient7 + " " + items.strMeasure7, items.strIngredient8 + " " + items.strMeasure8,
                items.strIngredient9 + " " + items.strMeasure9, items.strIngredient10 + " " + items.strMeasure10, items.strIngredient11 + " " + items.strMeasure11, items.strIngredient12 + " " + items.strMeasure12,
                items.strIngredient13 + " " + items.strMeasure13, items.strIngredient14 + " " + items.strMeasure14, items.strIngredient15 + " " + items.strMeasure15, items.strIngredient16 + " " + items.strMeasure16,
                items.strIngredient17 + " " + items.strMeasure17, items.strIngredient18 + " " + items.strMeasure18, items.strIngredient19 + " " + items.strMeasure19, items.strIngredient20 + " " + items.strMeasure20
            ).filterNotNull()

            binding.rvIngredient.layoutManager = GridLayoutManager(this@DetailActivity, 2)
            binding.rvIngredient.adapter = IngredientAdapter(ingredients)

            tvInstruction.text = items.strInstructions
        }
    }

//    private fun setupToolbar() {
//        setSupportActionBar(binding.topAppbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        binding.topAppbar.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                showToast("Bookmark clicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val r = (Color.red(color1) * inverseRatio + Color.red(color2) * ratio).toInt()
        val g = (Color.green(color1) * inverseRatio + Color.green(color2) * ratio).toInt()
        val b = (Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio).toInt()
        return Color.rgb(r, g, b)
    }
}