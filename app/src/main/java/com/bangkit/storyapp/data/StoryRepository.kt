package com.bangkit.storyapp.data

import com.bangkit.storyapp.data.api.AddStoryResponse
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.api.DetailStoryResponse
import com.bangkit.storyapp.data.api.LoginResponse
import com.bangkit.storyapp.data.api.RegisterResponse
import com.bangkit.storyapp.data.api.StoryResponse
import com.bangkit.storyapp.data.pref.UserModel
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.utils.parseErrorMessage
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
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

    suspend fun getStories(): StoryResponse {
        return try {
            apiService.getStories()
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addStory(
        description: RequestBody,
        image: MultipartBody.Part,
        lat: Float?,
        lon: Float?
    ): AddStoryResponse {
        return try {
            apiService.addStory(description, image, lat, lon)
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return try{
            apiService.getDetailStory(id)
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw e
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }
}