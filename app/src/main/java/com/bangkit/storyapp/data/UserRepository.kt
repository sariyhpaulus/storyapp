package com.bangkit.storyapp.data

import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.api.LoginResponse
import com.bangkit.storyapp.data.api.RegisterResponse
import com.bangkit.storyapp.data.pref.UserModel
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.utils.parseErrorMessage
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {
                response
            } else {
                throw Exception(response.message ?: "An unknown error occurred")
            }
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}