package com.example.flavorfinder.view.ui.dashboard

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
import com.example.flavorfinder.databinding.FragmentDashboardBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.GetBookmarkRecipe
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.adapter.BookmarkListAdapter
import com.example.flavorfinder.view.ui.detail.DetailActivity
import com.example.flavorfinder.view.ui.signin.SigninActivity
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

    private fun observeViewModel() {
        viewModel.bookmarks.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Succes -> {
                    val bookmarks = result.data.data.map { it.recipe }
                    if (bookmarks.isEmpty()) {
                        binding.rvBookmark.visibility = View.GONE
                        binding.tvNoItemBookmark.visibility = View.VISIBLE
                        showLoading(false)
                    } else {
                        binding.rvBookmark.visibility = View.VISIBLE
                        binding.tvNoItemBookmark.visibility = View.GONE
                        bookmarkListAdapter.submitList(bookmarks)
                        showLoading(false)
                    }
                }
                is Result.Error -> {
                    handleError(result.error)
                    showLoading(false)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
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
                navigateToDetailActivity(data)
            }
        })
    }

    private fun navigateToDetailActivity(bookmarkRecipe: GetBookmarkRecipe) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("bookmark", bookmarkRecipe)
        startActivity(intent)
    }

    private fun handleError(errorMessage: String) {
        when {
            errorMessage.contains("403") -> {
                lifecycleScope.launch {
                    userPreference.logout()
                    val intent = Intent(requireContext(), SigninActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            errorMessage.contains("404") -> {
                binding.tvNoItemBookmark.visibility = View.VISIBLE
            }
            else -> {
                showToast(errorMessage)
            }
        }
        binding.rvBookmark.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DashboardFragment"
    }
}
