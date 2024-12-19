package com.bangkit.storyapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bangkit.storyapp.data.response.AddStoryResponse
import com.bangkit.storyapp.data.api.ApiService
import com.bangkit.storyapp.data.response.DetailStoryResponse
import com.bangkit.storyapp.data.response.LoginResponse
import com.bangkit.storyapp.data.response.RegisterResponse
import com.bangkit.storyapp.data.response.StoryResponse
import com.bangkit.storyapp.data.pref.UserModel
import com.bangkit.storyapp.data.pref.UserPreference
import com.bangkit.storyapp.data.response.ListStoryItem
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

    fun getStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).flow
    }

    suspend fun addStory(
        description: RequestBody,
        image: MultipartBody.Part,
        lat: Double?,
        lon: Double?
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

    suspend fun getStoriesWithLocation(location: Int = 1): StoryResponse {
        return try {
            apiService.getStoriesWithLocation(location)
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