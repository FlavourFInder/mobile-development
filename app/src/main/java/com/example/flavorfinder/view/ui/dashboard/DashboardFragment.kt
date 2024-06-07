package com.example.flavorfinder.view.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfinder.databinding.FragmentDashboardBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.GetBookmarkRecipe
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.adapter.BookmarkListAdapter
import com.example.flavorfinder.view.ui.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(this.requireContext())
    }
    private lateinit var bookmarkListAdapter: BookmarkListAdapter
    private lateinit var userPreference: UserPreference
    private var repository: MealRepository? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        setupRecyclerView()
        observeViewModel()

        lifecycleScope.launch {
            val token = userPreference.getUser().token.first()
            token?.let {
                viewModel.getBookmarks()
            }
        }
    }

    private fun detailMenu(data: MealsItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("data", data)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.bookmarks.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Succes -> {
                    bookmarkListAdapter.submitList(result.data.data.map { it.recipe })
                }
                is Result.Error -> showToast(result.error)
                is Result.Loading -> showToast("Loading...")
            }
        }
    }

    private fun setupRecyclerView() {
        bookmarkListAdapter = BookmarkListAdapter()
        binding.rvBookmark.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarkListAdapter
        }
        bookmarkListAdapter.setOnItemClickCallback(object : BookmarkListAdapter.OnItemClickCallback {
            override fun onItemClick(data: GetBookmarkRecipe) {
                data.idMeal.let { lookupMealDetails(it) }
            }
        })
    }

    private fun lookupMealDetails(mealId: String) {
        val mealRepository = repository ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mealRepository.lookupMeals(mealId)
                val mealItem = response.meals.firstOrNull()
                if (mealItem != null) {
                    withContext(Dispatchers.Main) {
                        detailMenu(mealItem)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("meal detail not found")
                    }
                }
            } catch (e: Exception) {
                Log.e(DashboardFragment.TAG, "Lookup meal details failed", e)
                withContext(Dispatchers.Main) {
                    showToast("Lookup meal detail failed")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DashboardFragment"
    }
}