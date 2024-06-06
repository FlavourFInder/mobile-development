package com.example.flavorfinder.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flavorfinder.di.Injection
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.view.FilterIngredientViewModel
import com.example.flavorfinder.view.ui.main.MainViewModel
import com.example.flavorfinder.view.ui.register.RegisterViewModel
import com.example.flavorfinder.view.ui.signin.SignInViewModel
import com.example.flavorfinder.view.ui.home.HomeViewModel
import com.example.flavorfinder.view.ui.profile.ProfileViewModel

class ViewModelFactory(private val repository: MealRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FilterIngredientViewModel::class.java) -> {
                FilterIngredientViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                instance = repository?.let { ViewModelFactory(it) }
                instance!!
            }
        }
    }
}