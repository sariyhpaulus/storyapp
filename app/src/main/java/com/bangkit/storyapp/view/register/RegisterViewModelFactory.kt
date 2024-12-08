package com.bangkit.storyapp.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.storyapp.data.api.ApiService

class RegisterViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}