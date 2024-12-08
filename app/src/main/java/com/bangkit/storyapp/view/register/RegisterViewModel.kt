package com.bangkit.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.api.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val apiService: ApiService) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.register(name, email, password)
                _registerResult.value = Result.success(response)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}