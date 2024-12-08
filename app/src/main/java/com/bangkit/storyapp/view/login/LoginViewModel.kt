package com.bangkit.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.storyapp.data.UserRepository
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.api.LoginResponse
import com.bangkit.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService
) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(email, password)
                if (response.error == false && response.loginResult != null) {
                    val userModel = UserModel(
                        email = email,
                        token = response.loginResult.token ?: "",
                        isLogin = true
                    )
                    repository.saveSession(userModel)
                    _loginResult.value = Result.success(response)
                } else {
                    _loginResult.value = Result.failure(Exception(response.message ?: "Login failed"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}