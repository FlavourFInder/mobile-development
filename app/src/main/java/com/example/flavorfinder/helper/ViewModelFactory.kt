package com.example.flavorfinder.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flavorfinder.di.Injection
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.view.ui.home.HomeFragment
import com.example.flavorfinder.view.ui.home.HomeViewModel

class ViewModelFactory(private val repository: MealRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: HomeFragment): ViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                instance = ViewModelFactory(repository)
                instance!!
            }
        }
    }
}