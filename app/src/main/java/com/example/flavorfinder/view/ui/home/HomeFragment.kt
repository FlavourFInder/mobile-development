package com.example.flavorfinder.view.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flavorfinder.databinding.FragmentHomeBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.view.ui.adapter.MealListAdapter
import com.example.flavorfinder.view.ui.adapter.SearchResultAdapter
import com.example.flavorfinder.view.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this.requireContext())
    }
    private lateinit var menuListAdapter: MealListAdapter
    private var searchResultAdapter: SearchResultAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        getData()

        lifecycleScope.launch {
            homeViewModel.getUserData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        menuListAdapter = MealListAdapter()
        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuListAdapter
        }
        menuListAdapter.setOnItemClickCallback(object : MealListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MealsItem) {
                detailMenu(data)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        homeViewModel.searchResults.observe(viewLifecycleOwner) { meals ->
            if (meals != null) {
                searchResultAdapter = SearchResultAdapter(meals)
                searchResultAdapter?.setOnItemClickCallback(object : SearchResultAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: MealsItem) {
                        detailMenu(data)
                    }
                })
                binding.rvHome.adapter = searchResultAdapter
                searchResultAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun detailMenu(data: MealsItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("data", data)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        homeViewModel.meal.observe(viewLifecycleOwner) {
            if (searchResultAdapter == null) {
                menuListAdapter.submitData(lifecycle, it)
            }
        }

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            when (user) {
                is Result.Succes -> {
                    val userData = user.data.data
                    binding.tvGreeting.text = "Hi, ${userData.username}! Do you have something to cook?"
                }
                is Result.Error -> {
                    showToast("Failed to load user")
                }
                is Result.Loading -> { }
            }
        }
    }

    fun performSearch(query: String) {
        homeViewModel.searchMeals(query)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
