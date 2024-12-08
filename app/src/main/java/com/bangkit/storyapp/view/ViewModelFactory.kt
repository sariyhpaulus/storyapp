package com.bangkit.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.storyapp.data.UserRepository
import com.bangkit.storyapp.data.api.ApiConfig
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.di.Injection
import com.bangkit.storyapp.view.login.LoginViewModel
import com.bangkit.storyapp.view.main.MainViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val apiService: ApiService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository, apiService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val apiService = ApiConfig.getApiService()
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(repository, apiService)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}